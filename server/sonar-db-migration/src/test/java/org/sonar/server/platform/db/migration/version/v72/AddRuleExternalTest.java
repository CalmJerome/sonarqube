/*
 * SonarQube
 * Copyright (C) 2009-2019 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.server.platform.db.migration.version.v72;

import java.sql.SQLException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.sonar.db.CoreDbTester;

import static java.sql.Types.BOOLEAN;
import static org.junit.rules.ExpectedException.none;
import static org.sonar.db.CoreDbTester.createForSchema;

public class AddRuleExternalTest {
  @Rule
  public final CoreDbTester dbTester = createForSchema(AddRuleExternalTest.class, "rules.sql");

  @Rule
  public ExpectedException expectedException = none();

  private AddRuleExternal underTest = new AddRuleExternal(dbTester.database());

  @Test
  public void column_are_added_to_table() throws SQLException {
    dbTester.assertColumnDoesNotExist("rules", "is_external");

    underTest.execute();

    dbTester.assertColumnDefinition("rules", "is_external", BOOLEAN, null, true);
  }

  @Test
  public void migration_is_not_reentrant() throws SQLException {
    underTest.execute();

    expectedException.expect(IllegalStateException.class);

    underTest.execute();
  }
}
