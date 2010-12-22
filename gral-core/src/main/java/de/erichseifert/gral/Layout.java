/*
 * GRAL: GRAphing Library for Java(R)
 *
 * (C) Copyright 2009-2010 Erich Seifert <dev[at]erichseifert.de>,
 * Michael Seifert <michael.seifert[at]gmx.net>
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
package de.erichseifert.gral;

import java.awt.geom.Dimension2D;

/**
 * Interface that provides basic functions for arranging a layout.
 * Functionality includes the arrangement of the layout itself and
 * returning the preferred size of a container.
 */
public interface Layout {

	/**
	 * Arranges the components of this Container according to this Layout.
	 * @param container Container to be laid out.
	 */
	void layout(Container container);

	/**
	 * Returns the preferred size of the specified Container using this Layout.
	 * @param container Container whose preferred size is to be returned.
	 * @return Preferred extent of the specified Container.
	 */
	Dimension2D getPreferredSize(Container container);
}
