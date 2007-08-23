package org.liquibase.eclipse.common.change.wizard;

import liquibase.migrator.change.AddAutoIncrementChange;
import liquibase.migrator.change.Change;
import org.eclipse.datatools.connectivity.sqm.core.rte.jdbc.JDBCColumn;
import org.eclipse.datatools.modelbase.sql.schema.Database;
import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.jface.wizard.IWizardPage;

import java.sql.Connection;

public class AddAutoIncrementWizard extends BaseRefactorWizard {

	private Column column;
	
	public AddAutoIncrementWizard(Database database, Connection connection, Column column) {
		super(database, connection);
		this.column = column;
	}

	@Override
	protected Change[] createChanges() {
		AddAutoIncrementChange change = new AddAutoIncrementChange();
		change.setTableName(column.getTable().getName());
		change.setColumnName(column.getName());
		change.setColumnDataType(column.getDataType().getName());
		
		return new Change[] { change };
	}

	@Override
	protected IWizardPage[] createPages() {
		return new IWizardPage[0];
	}

	@Override
	protected void refresh() {
		((JDBCColumn)column).refresh();		
	}

}
