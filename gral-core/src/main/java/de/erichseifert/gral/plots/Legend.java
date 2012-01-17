/*
 * GRAL: GRAphing Library for Java(R)
 *
 * (C) Copyright 2009-2012 Erich Seifert <dev[at]erichseifert.de>,
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
package de.erichseifert.gral.plots;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Dimension2D;
import java.util.HashMap;
import java.util.Map;

import de.erichseifert.gral.data.DataChangeEvent;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.graphics.AbstractDrawable;
import de.erichseifert.gral.graphics.Drawable;
import de.erichseifert.gral.graphics.DrawableContainer;
import de.erichseifert.gral.graphics.DrawingContext;
import de.erichseifert.gral.graphics.EdgeLayout;
import de.erichseifert.gral.graphics.Layout;
import de.erichseifert.gral.graphics.StackedLayout;
import de.erichseifert.gral.plots.settings.Key;
import de.erichseifert.gral.plots.settings.SettingChangeEvent;
import de.erichseifert.gral.util.GraphicsUtils;
import de.erichseifert.gral.util.Insets2D;
import de.erichseifert.gral.util.Location;
import de.erichseifert.gral.util.Orientation;


/**
 * <p>Abstract class that serves as a base for legends in plots.
 * It stores a list of of items of type {@code Item} are used to
 * display a symbol and label for each (visible) data source.</p>
 * <p>Like other elements legends can be styled using various settings. The
 * settings are used to control to control how the legend, and its items
 * are displayed. The actual rendering of symbols has to be implemented by
 * derived classes.</p>
 */
public abstract class Legend extends StylableContainer {
	/** Version id for serialization. */
	private static final long serialVersionUID = -1561976879958765700L;

	/** Key for specifying the {@link java.awt.Paint} instance to be used to
	 paint the background. */
	public static final Key BACKGROUND =
		new Key("legend.background"); //$NON-NLS-1$
	/** Key for specifying the {@link java.awt.Stroke} instance to be used to
	 paint the border of the legend. */
	public static final Key BORDER =
		new Key("legend.border"); //$NON-NLS-1$
	/** Key for specifying the {@link java.awt.Font} instance to be used to
	 display the legend labels. */
	public static final Key FONT =
		new Key("legend.font"); //$NON-NLS-1$
	/** Key for specifying the {@link java.awt.Paint} instance to be used to
	 fill the border of the legend. */
	public static final Key COLOR =
		new Key("legend.color"); //$NON-NLS-1$
	/** Key for specifying a {@link de.erichseifert.gral.util.Orientation}
	instance defining the direction of the legend's items. */
	public static final Key ORIENTATION =
		new Key("legend.orientation"); //$NON-NLS-1$
	/** Key for specifying a {@link Number} value describing the horizontal
	alignment of the legend relative to the plot area. {@code 0.0} means left,
	{@code 0.5} means centered, and {@code 1.0} means right. */
	public static final Key ALIGNMENT_X =
		new Key("legend.alignment.x"); //$NON-NLS-1$
	/** Key for specifying a {@link Number} value describing the vertical
	alignment of the legend relative to the plot area. {@code 0.0} means top,
	{@code 0.5} means centered, and {@code 1.0} means bottom. */
	public static final Key ALIGNMENT_Y =
		new Key("legend.alignment.x"); //$NON-NLS-1$
	/** Key for specifying a {@link java.awt.Insets2D} instance defining the
	horizontal and vertical gap between items. The gap size is defined
	relative to the font height of the legend. */
	public static final Key GAP =
		new Key("legend.gap"); //$NON-NLS-1$
	/** Key for specifying a {@link java.awt.Insets2D} instance defining the
	size of the legend's symbols. The symbol size is defined relative to the
	font height of the legend. */
	public static final Key SYMBOL_SIZE =
		new Key("legend.symbol.size"); //$NON-NLS-1$

	/** Mapping of data sources to drawable components. */
	private final Map<DataSource, Drawable> components;

	/**
	 * Class that displays a specific data source as an item of a legend.
	 */
	protected class Item extends DrawableContainer {
		/** Version id for serialization. */
		private static final long serialVersionUID = 3401141040936913098L;

		/** Data source that is related to this item. */
		private final DataSource data;
		/** Symbol that should be drawn. */
		private final Drawable symbol;
		/** Label string that should be drawn. */
		private final Label label;

		/**
		 * Creates a new Item object with the specified data source and text.
		 * @param data Data source to be displayed.
		 * @param labelText Description text.
		 */
		public Item(final DataSource data, final String labelText) {
			double fontSize =
				Legend.this.<Font>getSetting(FONT).getSize2D();
			setLayout(new EdgeLayout(fontSize, 0.0));
			this.data = data;

			symbol = new AbstractDrawable() {
				/** Version id for serialization. */
				private static final long serialVersionUID = -8342826016352506818L;

				public void draw(DrawingContext context) {
					drawSymbol(context, this, Item.this.data);
				}

				@Override
				public Dimension2D getPreferredSize() {
					double fontSize =
						Legend.this.<Font>getSetting(FONT).getSize2D();
					Dimension2D symbolSize =
						Legend.this.getSetting(SYMBOL_SIZE);
					Dimension2D size = super.getPreferredSize();
					size.setSize(symbolSize.getWidth()*fontSize,
							symbolSize.getHeight()*fontSize);
					return size;
				}
			};
			add(symbol, Location.WEST);

			label = new Label(labelText);
			label.setSetting(Label.FONT, Legend.this.<Font>getSetting(FONT));
			label.setSetting(Label.ALIGNMENT_X, 0.0);
			label.setSetting(Label.ALIGNMENT_Y, 0.5);
			add(label, Location.CENTER);
		}

		/**
		 * Returns the displayed data source.
		 * @return Displayed data source
		 */
		public DataSource getData() {
			return data;
		}
	}

	/**
	 * Initializes a new instance with a default background color, a border,
	 * vertical orientation and a gap between the items. The default alignment
	 * is set to top-left.
	 */
	public Legend() {
		setInsets(new Insets2D.Double(10.0));

		components = new HashMap<DataSource, Drawable>();

		setSettingDefault(BACKGROUND, Color.WHITE);
		setSettingDefault(BORDER, new BasicStroke(1f));
		setSettingDefault(FONT, Font.decode(null));
		setSettingDefault(COLOR, Color.BLACK);
		setSettingDefault(ORIENTATION, Orientation.VERTICAL);
		setSettingDefault(ALIGNMENT_X, 0.0);
		setSettingDefault(ALIGNMENT_Y, 0.0);
		setSettingDefault(GAP, new de.erichseifert.gral.util.Dimension2D.Double(2.0, 0.5));
		setSettingDefault(SYMBOL_SIZE, new de.erichseifert.gral.util.Dimension2D.Double(2.0, 2.0));
	}

	/**
	 * Draws the {@code Drawable} with the specified drawing context.
	 * @param context Environment used for drawing
	 */
	@Override
	public void draw(DrawingContext context) {
		drawBackground(context);
		drawBorder(context);
		drawComponents(context);
	}

	/**
	 * Draws the background of this legend with the specified drawing context.
	 * @param context Environment used for drawing.
	 */
	protected void drawBackground(DrawingContext context) {
		Paint bg = getSetting(BACKGROUND);
		if (bg != null) {
			GraphicsUtils.fillPaintedShape(
				context.getGraphics(), getBounds(), bg, null);
		}
	}

	/**
	 * Draws the border of this legend with the specified drawing context.
	 * @param context Environment used for drawing.
	 */
	protected void drawBorder(DrawingContext context) {
		Stroke stroke = getSetting(BORDER);
		if (stroke != null) {
			Paint fg = getSetting(COLOR);
			GraphicsUtils.drawPaintedShape(
				context.getGraphics(), getBounds(), fg, null, stroke);
		}
	}

	/**
	 * Draws the symbol of a certain data source.
	 * @param context Settings for drawing.
	 * @param symbol symbol to draw.
	 * @param data Data source.
	 */
	protected abstract void drawSymbol(
		DrawingContext context,
		Drawable symbol, DataSource data);

	/**
	 * Adds the specified data source in order to display it.
	 * @param source data source to be added.
	 */
	public void add(DataSource source) {
		Item item = new Item(source, source.toString());
		add(item);
		components.put(source, item);
	}

	/**
	 * Returns whether the specified data source was added to the legend.
	 * @param source Data source
	 * @return {@code true} if legend contains the data source, otherwise {@code false}
	 */
	public boolean contains(DataSource source) {
		return components.containsKey(source);
	}

	/**
	 * Removes the specified data source.
	 * @param source Data source to be removed.
	 */
	public void remove(DataSource source) {
		Drawable removeItem = components.get(source);
		if (removeItem != null) {
			remove(removeItem);
		}
		components.remove(source);
	}

	/**
	 * Removes all data sources from the legend.
	 */
	public void clear() {
		for (DataSource source : components.keySet()) {
			remove(source);
		}
	}

	/**
	 * Notify all listeners that data has changed.
	 * @param events Event objects describing the values that have changed.
	 */
	protected void notifyDataChanged(DataChangeEvent... events) {
		// FIXME Is this function really necessary?
		layout();
	}

	/**
	 * Invoked if a setting has changed.
	 * @param event Event containing information about the changed setting.
	 */
	@Override
	public void settingChanged(SettingChangeEvent event) {
		Key key = event.getKey();
		if (ORIENTATION.equals(key) || GAP.equals(key)) {
			Orientation orientation = getSetting(ORIENTATION);
			Dimension2D gap = getSetting(GAP);
			if (GAP.equals(key) && gap != null) {
				double fontSize = this.<Font>getSetting(FONT).getSize2D();
				gap.setSize(gap.getWidth()*fontSize, gap.getHeight()*fontSize);
			}
			Layout layout = new StackedLayout(orientation, gap);
			setLayout(layout);
		} else if (FONT.equals(key)) {
			for (Drawable item : components.values()) {
				if (!(item instanceof Item)) {
					continue;
				}
				((Item) item).label.setSetting(Label.FONT,
					this.<Font>getSetting(FONT));
			}
		}
	}

	@Override
	public void setBounds(double x, double y, double width, double height) {
		Dimension2D size = getPreferredSize();
		double alignX = this.<Number>getSetting(ALIGNMENT_X).doubleValue();
		double alignY = this.<Number>getSetting(ALIGNMENT_Y).doubleValue();
		super.setBounds(
			x + alignX*(width - size.getWidth()),
			y + alignY*(height - size.getHeight()),
			size.getWidth(),
			size.getHeight()
		);
	}
}