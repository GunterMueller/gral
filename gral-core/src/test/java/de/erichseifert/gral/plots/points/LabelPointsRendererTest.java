/*
 * GRAL: GRAphing Library for Java(R)
 *
 * (C) Copyright 2009-2013 Erich Seifert <dev[at]erichseifert.de>,
 * Michael Seifert <michael[at]erichseifert.de>
 *
 * This file is part of GRAL.
 *
 * GRAL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GRAL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GRAL.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.erichseifert.gral.plots.points;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.awt.Shape;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import de.erichseifert.gral.TestUtils;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.data.Row;
import de.erichseifert.gral.plots.axes.Axis;
import de.erichseifert.gral.plots.axes.AxisRenderer;
import de.erichseifert.gral.plots.axes.LinearRenderer2D;

public class LabelPointsRendererTest {
	private static DataTable table;
	private static Row row;
	private static Axis axis;
	private static AxisRenderer axisRenderer;
	private static PointData data;

	@BeforeClass
	@SuppressWarnings("unchecked")
	public static void setUpBeforeClass() {
		table = new DataTable(Integer.class, Integer.class, Integer.class);
		table.add(1, 3, 1);              // 0
		table.add(2, (Integer) null, 2); // 1

		row = new Row(table, 0);

		axis = new Axis(-1.0, 1.0);
		axisRenderer = new LinearRenderer2D();
		axisRenderer.setShape(new Line2D.Double(-5.0, 0.0, 5.0, 0.0));

		data = new PointData(Arrays.asList(axis), Arrays.asList(axisRenderer), row, 0);
	}

	@Test
	public void testPointPath() {
		PointRenderer r = new LabelPointRenderer();
		Shape path = r.getPointShape(data);
		assertNotNull(path);
	}

	@Test
	public void testInvalidColumn() {
		PointRenderer r = new LabelPointRenderer();
		r.setSetting(LabelPointRenderer.COLUMN, table.getColumnCount());
		Shape path = r.getPointShape(data);
		assertNull(path);
	}

	@Test
	public void testNullLabel() {
		PointRenderer r = new LabelPointRenderer();
		r.setSetting(LabelPointRenderer.COLUMN, 1);
		Row row2 = new Row(table, 1);
		assertNull(row2.get(1));
		PointData data2 = new PointData(data.axes, data.axisRenderers, row2, 0);
		Shape path = r.getPointShape(data2);
		assertNull(path);
	}

	@Test
	public void testSerialization() throws IOException, ClassNotFoundException {
		PointRenderer original = new LabelPointRenderer();
		PointRenderer deserialized = TestUtils.serializeAndDeserialize(original);

		TestUtils.assertSettings(original, deserialized);
    }
}
