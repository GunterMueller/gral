/*
 * GRAL: GRAphing Library for Java(R)
 *
 * (C) Copyright 2009-2010 Erich Seifert <info[at]erichseifert.de>, Michael Seifert <michael.seifert[at]gmx.net>
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

package de.erichseifert.gral.data.filters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import de.erichseifert.gral.data.DataTable;

public class ConvolutionTest {
	private static final double DELTA = 1e-15;
	private static DataTable table;
	private static Kernel kernel;

	@BeforeClass
	public static void setUpBeforeClass() {
		table = new DataTable(Double.class, Double.class);
		table.add(1.0, 1.0); // 0
		table.add(2.0, 1.0); // 1
		table.add(3.0, 1.0); // 2
		table.add(4.0, 1.0); // 3
		table.add(5.0, 1.0); // 4
		table.add(6.0, 1.0); // 5
		table.add(7.0, 1.0); // 6
		table.add(8.0, 1.0); // 7

		kernel = new Kernel(1.0, 1.0, 1.0);
	}

	@Test
	public void testCreation() {
		Convolution filter = new Convolution(table, kernel, Filter.Mode.ZERO, 0, 1);

		assertEquals(table.getColumnCount(), filter.getColumnCount());
		assertEquals(table.getRowCount(), filter.getRowCount());
	}

	@Test
	public void testKernel() {
		Convolution filter = new Convolution(table, kernel, Filter.Mode.ZERO, 0, 1);
		assertEquals(kernel, filter.getKernel());
	}

	@Test
	public void testMode() {
		Convolution filter = new Convolution(table, kernel, Filter.Mode.OMIT, 0, 1);

		for (Filter.Mode mode : Filter.Mode.values()) {
			filter.setMode(mode);
			assertEquals(mode, filter.getMode());
		}
	}

	@Test
	public void testColumns() {
		Convolution filter = new Convolution(table, kernel, Filter.Mode.ZERO, 0);

		assertEquals(3.0, filter.get(0, 0).doubleValue(), DELTA);
		assertEquals(6.0, filter.get(0, 1).doubleValue(), DELTA);
		assertEquals(9.0, filter.get(0, 2).doubleValue(), DELTA);

		assertEquals(1.0, filter.get(1, 0).doubleValue(), DELTA);
		assertEquals(1.0, filter.get(1, 1).doubleValue(), DELTA);
		assertEquals(1.0, filter.get(1, 2).doubleValue(), DELTA);
	}

	@Test
	public void testModeOmit() {
		Convolution filter = new Convolution(table, kernel, Filter.Mode.OMIT, 0, 1);

		assertTrue(Double.isNaN(filter.get(0, 0).doubleValue()));
		assertEquals(6.0, filter.get(0, 1).doubleValue(), DELTA);
		assertTrue(Double.isNaN(filter.get(0, 7).doubleValue()));

		assertTrue(Double.isNaN(filter.get(1, 0).doubleValue()));
		assertEquals(3.0, filter.get(1, 1).doubleValue(), DELTA);
		assertTrue(Double.isNaN(filter.get(1, 7).doubleValue()));
	}

	@Test
	public void testModeZero() {
		Convolution filter = new Convolution(table, kernel, Filter.Mode.ZERO, 0, 1);

		assertEquals( 3.0, filter.get(0, 0).doubleValue(), DELTA);
		assertEquals( 6.0, filter.get(0, 1).doubleValue(), DELTA);
		assertEquals(15.0, filter.get(0, 7).doubleValue(), DELTA);

		assertEquals( 2.0, filter.get(1, 0).doubleValue(), DELTA);
		assertEquals( 3.0, filter.get(1, 1).doubleValue(), DELTA);
		assertEquals( 2.0, filter.get(1, 7).doubleValue(), DELTA);
	}

	@Test
	public void testModeRepeat() {
		Convolution filter = new Convolution(table, kernel, Filter.Mode.REPEAT, 0, 1);

		assertEquals( 4.0, filter.get(0, 0).doubleValue(), DELTA);
		assertEquals( 6.0, filter.get(0, 1).doubleValue(), DELTA);
		assertEquals(23.0, filter.get(0, 7).doubleValue(), DELTA);

		assertEquals( 3.0, filter.get(1, 0).doubleValue(), DELTA);
		assertEquals( 3.0, filter.get(1, 1).doubleValue(), DELTA);
		assertEquals( 3.0, filter.get(1, 7).doubleValue(), DELTA);
	}

	@Test
	public void testModeMirror() {
		Convolution filter = new Convolution(table, kernel, Filter.Mode.MIRROR, 0, 1);

		assertEquals( 5.0, filter.get(0, 0).doubleValue(), DELTA);
		assertEquals( 6.0, filter.get(0, 1).doubleValue(), DELTA);
		assertEquals(22.0, filter.get(0, 7).doubleValue(), DELTA);

		assertEquals( 3.0, filter.get(1, 0).doubleValue(), DELTA);
		assertEquals( 3.0, filter.get(1, 1).doubleValue(), DELTA);
		assertEquals( 3.0, filter.get(1, 7).doubleValue(), DELTA);
	}

	@Test
	public void testModeCircular() {
		Convolution filter = new Convolution(table, kernel, Filter.Mode.CIRCULAR, 0, 1);

		assertEquals(11.0, filter.get(0, 0).doubleValue(), DELTA);
		assertEquals( 6.0, filter.get(0, 1).doubleValue(), DELTA);
		assertEquals(16.0, filter.get(0, 7).doubleValue(), DELTA);

		assertEquals( 3.0, filter.get(1, 0).doubleValue(), DELTA);
		assertEquals( 3.0, filter.get(1, 1).doubleValue(), DELTA);
		assertEquals( 3.0, filter.get(1, 7).doubleValue(), DELTA);
	}
}
