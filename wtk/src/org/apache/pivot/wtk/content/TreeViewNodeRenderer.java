/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.pivot.wtk.content;

import java.awt.Color;
import java.awt.Font;

import org.apache.pivot.wtk.Bounds;
import org.apache.pivot.wtk.FlowPane;
import org.apache.pivot.wtk.HorizontalAlignment;
import org.apache.pivot.wtk.ImageView;
import org.apache.pivot.wtk.Label;
import org.apache.pivot.wtk.TreeView;
import org.apache.pivot.wtk.VerticalAlignment;
import org.apache.pivot.wtk.media.Image;


/**
 * Default tree node renderer, which knows how to render instances of
 * {@link TreeNode} and {@link Image}. Anything else will be rendered as a
 * string (by calling <tt>node.toString()</tt>.
 *
 * @author gbrown
 */
public class TreeViewNodeRenderer extends FlowPane implements TreeView.NodeRenderer {
    protected ImageView imageView = new ImageView();
    protected Label label = new Label();

    public static final int DEFAULT_ICON_WIDTH = 16;
    public static final int DEFAULT_ICON_HEIGHT = 16;
    public static boolean DEFAULT_SHOW_ICON = true;

    public TreeViewNodeRenderer() {
        super();

        getStyles().put("horizontalAlignment", HorizontalAlignment.LEFT);
        getStyles().put("verticalAlignment", VerticalAlignment.CENTER);

        add(imageView);
        add(label);

        imageView.setPreferredSize(DEFAULT_ICON_WIDTH, DEFAULT_ICON_HEIGHT);
        imageView.setDisplayable(DEFAULT_SHOW_ICON);
    }

    public void setSize(int width, int height) {
        super.setSize(width, height);

        // Since this component doesn't have a parent, it won't be validated
        // via layout; ensure that it is valid here
        validate();
    }

    public void render(Object node, TreeView treeView, boolean expanded,
        boolean selected, TreeView.NodeCheckState checkState,
        boolean highlighted, boolean disabled) {
        if (node != null) {
            Image icon = null;
            String text = null;

            if (node instanceof TreeNode) {
                TreeNode treeNode = (TreeNode)node;

                if (expanded
                    && treeNode instanceof TreeBranch) {
                    TreeBranch treeBranch = (TreeBranch)treeNode;
                    icon = treeBranch.getExpandedIcon();

                    if (icon == null) {
                        icon = treeBranch.getIcon();
                    }
                } else {
                    icon = treeNode.getIcon();
                }

                text = treeNode.getText();
            } else if (node instanceof Image) {
                icon = (Image)node;
            } else {
                if (node != null) {
                    text = node.toString();
                }
            }

            // Update the image view
            imageView.setImage(icon);
            imageView.getStyles().put("opacity",
                (treeView.isEnabled() && !disabled) ? 1.0f : 0.5f);

            // Update the label
            label.setText(text);

            if (text == null) {
                label.setDisplayable(false);
            } else {
                label.setDisplayable(true);

                Font font = (Font)treeView.getStyles().get("font");
                label.getStyles().put("font", font);

                Color color;
                if (treeView.isEnabled() && !disabled) {
                    if (selected) {
                        if (treeView.isFocused()) {
                            color = (Color)treeView.getStyles().get("selectionColor");
                        } else {
                            color = (Color)treeView.getStyles().get("inactiveSelectionColor");
                        }
                    } else {
                        color = (Color)treeView.getStyles().get("color");
                    }
                } else {
                    color = (Color)treeView.getStyles().get("disabledColor");
                }

                label.getStyles().put("color", color);
            }
        }
    }

    public int getIconWidth() {
        return imageView.getPreferredWidth(-1);
    }

    public void setIconWidth(int iconWidth) {
        if (iconWidth == -1) {
            throw new IllegalArgumentException();
        }

        imageView.setPreferredWidth(iconWidth);
    }

    public int getIconHeight() {
        return imageView.getPreferredHeight(-1);
    }

    public void setIconHeight(int iconHeight) {
        if (iconHeight == -1) {
            throw new IllegalArgumentException();
        }

        imageView.setPreferredHeight(iconHeight);
    }

    public boolean getShowIcon() {
        return imageView.isDisplayable();
    }

    public void setShowIcon(boolean showIcon) {
        imageView.setDisplayable(showIcon);
    }

    /**
     * Gets the bounds of the text that is rendered by this renderer.
     *
     * @return
     * The bounds of the rendered text, or <tt>null</tt> if this renderer did
     * not render any text.
     */
    public Bounds getTextBounds() {
        return (label.isVisible() ? label.getBounds() : null);
    }
}
