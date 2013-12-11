package com.introspy.core;

public class SQLiteIntrospyLog {
	  private long id;
	  private String Row;

	  public long getId() {
	    return id;
	  }

	  public void setId(long id) {
	    this.id = id;
	  }

	  public String getRow() {
	    return Row;
	  }

	  public void setRow(String Row) {
	    this.Row = Row;
	  }

	  // Will be used by the ArrayAdapter in the ListView
	  @Override
	  public String toString() {
	    return Row;
	  }
}
