package liquibase.migrator.change;

import liquibase.database.Database;
import liquibase.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This class is the representation of the column tag in the XMl file
 * It has a reference to the Constraints object for getting information
 * about the columns constraints.
 */
public class ColumnConfig {
    private String name;
    private String type;
    private String value;
    private String valueNumeric;
    private String valueDate;
    private Boolean valueBoolean;

    private String defaultValue;
    private String defaultValueNumeric;
    private String defaultValueDate;
    private Boolean defaultValueBoolean;

    private ConstraintsConfig constraints;
    private Boolean autoIncrement;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }


    public void setValue(String value) {
        // Since we have two rules for the value it can either be specifed as an attribute
        // or as the tag body in case of long values then the check is necessary so that it
        // should not override the value specifed as an attribute.
        if (StringUtils.trimToNull(value) != null) {
            this.value = value;
        }
    }

    public String getValueNumeric() {
        return valueNumeric;
    }

    public void setValueNumeric(String valueNumeric) {
        this.valueNumeric = valueNumeric;
    }

    public Boolean getValueBoolean() {
        return valueBoolean;
    }

    public void setValueBoolean(Boolean valueBoolean) {
        this.valueBoolean = valueBoolean;
    }

    public String getValueDate() {
        return valueDate;
    }

    public void setValueDate(String valueDate) {
        this.valueDate = valueDate;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }


    public String getDefaultValueNumeric() {
        return defaultValueNumeric;
    }

    public void setDefaultValueNumeric(String defaultValueNumeric) {
        if ("GENERATED_BY_DEFAULT".equals(defaultValueNumeric)) {
            setAutoIncrement(true);
        } else {
            this.defaultValueNumeric = defaultValueNumeric;
        }
    }

    public String getDefaultValueDate() {
        return defaultValueDate;
    }

    public void setDefaultValueDate(String defaultValueDate) {
        this.defaultValueDate = defaultValueDate;
    }

    public Boolean getDefaultValueBoolean() {
        return defaultValueBoolean;
    }

    public void setDefaultValueBoolean(Boolean defaultValueBoolean) {
        this.defaultValueBoolean = defaultValueBoolean;
    }

    public ConstraintsConfig getConstraints() {
        return constraints;
    }

    public void setConstraints(ConstraintsConfig constraints) {
        this.constraints = constraints;
    }

    public Boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(Boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public Element createNode(Document document) {
        Element element = document.createElement("column");
        element.setAttribute("name", getName());
        if (getType() != null) {
            element.setAttribute("type", getType());
        }

        if (getDefaultValue() != null) {
            element.setAttribute("defaultValue", getDefaultValue());
        }
        if (getDefaultValueNumeric() != null) {
            element.setAttribute("defaultValueNumeric", getDefaultValueNumeric());
        }
        if (getDefaultValueDate() != null) {
            element.setAttribute("defaultValueDate", getDefaultValueDate());
        }
        if (getDefaultValueBoolean() != null) {
            element.setAttribute("defaultValueBoolean", getDefaultValueBoolean().toString());
        }

        if (getValue() != null) {
            element.setAttribute("value", getValue());
        }
        if (getValueNumeric() != null) {
            element.setAttribute("valueNumeric", getValueNumeric());
        }
        if (getValueBoolean() != null) {
            element.setAttribute("valueBoolean", getValueBoolean().toString());
        }
        if (getValueDate() != null) {
            element.setAttribute("valueDate", getValueDate());
        }

        if (isAutoIncrement() != null && isAutoIncrement()) {
            element.setAttribute("autoIncrement", "true");
        }

        ConstraintsConfig constraints = getConstraints();
        if (constraints != null) {
            Element constraintsElement = document.createElement("constraints");
            if (constraints.getCheck() != null) {
                constraintsElement.setAttribute("check", constraints.getCheck());
            }
            if (constraints.getForeignKeyName() != null) {
                constraintsElement.setAttribute("foreignKeyName", constraints.getForeignKeyName());
            }
            if (constraints.getReferences() != null) {
                constraintsElement.setAttribute("references", constraints.getReferences());
            }
            if (constraints.isDeferrable() != null) {
                constraintsElement.setAttribute("deferrable", constraints.isDeferrable().toString());
            }
            if (constraints.isDeleteCascade() != null) {
                constraintsElement.setAttribute("deleteCascade", constraints.isDeleteCascade().toString());
            }
            if (constraints.isInitiallyDeferred() != null) {
                constraintsElement.setAttribute("initiallyDeferred", constraints.isInitiallyDeferred().toString());
            }
            if (constraints.isNullable() != null) {
                constraintsElement.setAttribute("nullable", constraints.isNullable().toString());
            }
            if (constraints.isPrimaryKey() != null) {
                constraintsElement.setAttribute("primaryKey", constraints.isPrimaryKey().toString());
            }
            if (constraints.isUnique() != null) {
                constraintsElement.setAttribute("unique", constraints.isUnique().toString());
            }
            element.appendChild(constraintsElement);
        }

        return element;
    }

    public String getDefaultColumnValue(Database database) {
        if (this.getDefaultValue() != null) {
            if ("null".equalsIgnoreCase(this.getDefaultValue())) {
                return "NULL";
            }
            if (!database.shouldQuoteValue(this.getDefaultValue())) {
                return this.getDefaultValue();
            } else {
                return "'" + this.getDefaultValue().replaceAll("'", "''") + "'";
            }
        } else if (this.getDefaultValueNumeric() != null) {
            return this.getDefaultValueNumeric();
        } else if (this.getDefaultValueBoolean() != null) {
            String returnValue;
            if (this.getDefaultValueBoolean()) {
                returnValue = database.getTrueBooleanValue();
            } else {
                returnValue = database.getFalseBooleanValue();
            }

            if (returnValue.matches("\\d+")) {
                return returnValue;
            } else {
                return "'"+returnValue+"'";
            }
        } else if (this.getDefaultValueDate() != null) {
            return database.getDateLiteral(this.getDefaultValueDate());
        } else {
            return "NULL";
        }
    }

    public boolean hasDefaultValue() {
        return this.getDefaultValue() != null
                || this.getDefaultValueBoolean() != null
                || this.getDefaultValueDate() != null
                || this.getDefaultValueNumeric() != null;
    }

}
