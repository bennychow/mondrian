/*
// This software is subject to the terms of the Eclipse Public License v1.0
// Agreement, available at the following URL:
// http://www.eclipse.org/legal/epl-v10.html.
// You must accept the terms of that agreement to use this software.
//
// Copyright (c) 2002-2020 Hitachi Vantara..  All rights reserved.
*/
package mondrian.spi.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Implementation of {@link mondrian.spi.Dialect} for the Dremio database.
 *
 * @author Benny Chow
 */
public class DremioDialect extends JdbcDialectImpl {

  public static final JdbcDialectFactory FACTORY =
      new JdbcDialectFactory( DremioDialect.class, DatabaseProduct.DREMIO );

  /**
   * Creates a DremioDialect.
   *
   * @param connection
   *          Connection
   */
  public DremioDialect( Connection connection ) throws SQLException {
    super( connection );
  }

  @Override
  public DatabaseProduct getDatabaseProduct() {
    return DatabaseProduct.DREMIO;
  }

  @Override
  public boolean allowsRegularExpressionInWhereClause() {
    return true;
  }

  @Override
  public String generateRegularExpression( String source, String javaRegex ) {
    try {
      Pattern.compile( javaRegex );
    } catch ( PatternSyntaxException e ) {
      // Not a valid Java regex. Too risky to continue.
      return null;
    }

    final StringBuilder sb = new StringBuilder();
    sb.append( " REGEXP_LIKE ( ");
    sb.append( source );
    sb.append( ", " );
    quoteStringLiteral( sb, javaRegex );
    sb.append( ")" );
    return sb.toString();
  }
}

// End DremioDialect.java
