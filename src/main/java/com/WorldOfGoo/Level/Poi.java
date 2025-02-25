package com.WorldOfGoo.Level;

import com.WooGLEFX.Engine.Main;
import com.WooGLEFX.Engine.Renderer;
import com.WooGLEFX.Structures.EditorAttribute;
import com.WooGLEFX.Structures.SimpleStructures.DragSettings;
import com.WooGLEFX.Structures.EditorObject;
import com.WooGLEFX.Structures.InputField;
import com.WooGLEFX.Structures.SimpleStructures.MetaEditorAttribute;
import com.WooGLEFX.Structures.SimpleStructures.Position;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public class Poi extends EditorObject {
    public Poi(EditorObject _parent) {
        super(_parent);
        setRealName("poi");
        addAttribute("pos", "0,0", InputField.POSITION, true);
        addAttribute("traveltime", "3", InputField.NUMBER, true);
        addAttribute("pause", "0", InputField.NUMBER, true);
        addAttribute("zoom", "1", InputField.NUMBER, true);
        setNameAttribute(new EditorAttribute(this, "", "", "", new InputField("", InputField.NULL), false));
        setMetaAttributes(MetaEditorAttribute.parse("pos,traveltime,pause,zoom,"));
    }

    @Override
    public void draw (GraphicsContext graphicsContext, GraphicsContext imageGraphicsContext) {

        if (Main.getLevel().isShowCameras()) {

            Position pos = Position.parse(getAttribute("pos"));
            double zoom = getDouble("zoom");

            graphicsContext.setLineWidth(1);

            double width = 0;
            double height = 525 / zoom;
            if (getParent().getAttribute("aspect").equals("normal")) {
                graphicsContext.setStroke(Renderer.poiNormal);
                graphicsContext.setFill(Renderer.poiNormal);
                width = 800 / zoom;
            } else if (getParent().getAttribute("aspect").equals("widescreen")) {
                graphicsContext.setStroke(Renderer.poiWidescreen);
                graphicsContext.setFill(Renderer.poiWidescreen);
                width = 1007 / zoom;
            }

            double cinematicBarHeight = 70 / zoom;

            double x = pos.getX() - width / 2;
            double y = -pos.getY() - height / 2;

            if (getParent().getChildren().indexOf(this) != getParent().getChildren().size() - 1) {
                graphicsContext.strokeRect(x * Main.getLevel().getZoom() + Main.getLevel().getOffsetX() + 0.5, (y + cinematicBarHeight) * Main.getLevel().getZoom() + Main.getLevel().getOffsetY(), width * Main.getLevel().getZoom() - 1, (height - cinematicBarHeight * 2) * Main.getLevel().getZoom());
                graphicsContext.fillRect(x * Main.getLevel().getZoom() + Main.getLevel().getOffsetX(), y * Main.getLevel().getZoom() + Main.getLevel().getOffsetY(), width * Main.getLevel().getZoom(), cinematicBarHeight * Main.getLevel().getZoom());
                graphicsContext.fillRect(x * Main.getLevel().getZoom() + Main.getLevel().getOffsetX(), (y + height - cinematicBarHeight) * Main.getLevel().getZoom() + Main.getLevel().getOffsetY(), width * Main.getLevel().getZoom(), cinematicBarHeight * Main.getLevel().getZoom());
            } else {
                graphicsContext.strokeRect(x * Main.getLevel().getZoom() + Main.getLevel().getOffsetX(), y * Main.getLevel().getZoom() + Main.getLevel().getOffsetY(), width * Main.getLevel().getZoom(), height * Main.getLevel().getZoom());
            }

            if (this == Main.getSelected()) {
                graphicsContext.setStroke(Renderer.selectionOutline2);
                graphicsContext.setLineWidth(1);
                graphicsContext.setLineDashes(3);
                graphicsContext.setLineDashOffset(0);
                graphicsContext.strokeRect(x * Main.getLevel().getZoom() + Main.getLevel().getOffsetX(), y * Main.getLevel().getZoom() + Main.getLevel().getOffsetY(), width * Main.getLevel().getZoom(), height * Main.getLevel().getZoom());
                graphicsContext.setStroke(Renderer.selectionOutline);
                graphicsContext.setLineWidth(1);
                graphicsContext.setLineDashOffset(3);
                graphicsContext.strokeRect(x * Main.getLevel().getZoom() + Main.getLevel().getOffsetX(), y * Main.getLevel().getZoom() + Main.getLevel().getOffsetY(), width * Main.getLevel().getZoom(), height * Main.getLevel().getZoom());
                graphicsContext.setLineDashes(0);

                Point2D topRight = new Point2D(x + width, y).multiply(Main.getLevel().getZoom());
                Point2D topLeft = new Point2D(x, y).multiply(Main.getLevel().getZoom());
                Point2D bottomLeft = new Point2D(x, y + height).multiply(Main.getLevel().getZoom());
                Point2D bottomRight = new Point2D(x + width, y + height).multiply(Main.getLevel().getZoom());

                graphicsContext.setLineWidth(1);
                graphicsContext.strokeRect(topRight.getX() + Main.getLevel().getOffsetX() - 4, topRight.getY() + Main.getLevel().getOffsetY() - 4, 8, 8);
                graphicsContext.strokeRect(topLeft.getX() + Main.getLevel().getOffsetX() - 4, topLeft.getY() + Main.getLevel().getOffsetY() - 4, 8, 8);
                graphicsContext.strokeRect(bottomLeft.getX() + Main.getLevel().getOffsetX() - 4, bottomLeft.getY() + Main.getLevel().getOffsetY() - 4, 8, 8);
                graphicsContext.strokeRect(bottomRight.getX() + Main.getLevel().getOffsetX() - 4, bottomRight.getY() + Main.getLevel().getOffsetY() - 4, 8, 8);
            }
        }
    }

    @Override
    public DragSettings mouseIntersection (double mX, double mY) {

        if (Main.getLevel().isShowCameras()) {

            Position pos = Position.parse(getAttribute("pos"));
            double zoom = getDouble("zoom");

            double width = 0;
            double height = 525 / zoom;
            if (getParent().getAttribute("aspect").equals("normal")) {
                width = 800 / zoom;
            } else if (getParent().getAttribute("aspect").equals("widescreen")) {
                width = 1007 / zoom;
            }

            double x = pos.getX() - width / 2;
            double y = -pos.getY() - height / 2;

            if (mX > x && mY > y && mX < x + width && mY < y + height) {
                DragSettings dragSettings = new DragSettings(DragSettings.MOVE);
                dragSettings.setInitialSourceX(mX - pos.getX());
                dragSettings.setInitialSourceY(mY + pos.getY());
                return dragSettings;
            } else {
                return new DragSettings(DragSettings.NONE);
            }
        } else {
            return new DragSettings(DragSettings.NONE);
        }
    }
    @Override
    public DragSettings mouseIntersectingCorners(double mX2, double mY2) {

        if (Main.getLevel().isShowCameras()) {

            Position pos = Position.parse(getAttribute("pos"));
            double zoom = getDouble("zoom");

            double width = 0;
            double height = 525 / zoom;
            if (getParent().getAttribute("aspect").equals("normal")) {
                width = 800 / zoom;
            } else if (getParent().getAttribute("aspect").equals("widescreen")) {
                width = 1007 / zoom;
            }

            double x2 = pos.getX();
            double y2 = pos.getY();

            Point2D topRight = new Point2D(x2 + width / 2, y2 + height / 2);
            Point2D topLeft = new Point2D(x2 - width / 2, y2 + height / 2);
            Point2D bottomLeft = new Point2D(x2 - width / 2, y2 - height / 2);
            Point2D bottomRight = new Point2D(x2 + width / 2, y2 - height / 2);
            topRight = new Point2D((topRight.getX()), (-topRight.getY()));
            topLeft = new Point2D((topLeft.getX()), (-topLeft.getY()));
            bottomLeft = new Point2D((bottomLeft.getX()), (-bottomLeft.getY()));
            bottomRight = new Point2D((bottomRight.getX()), (-bottomRight.getY()));
            double distance = 4 / Main.getLevel().getZoom();

            double x = pos.getX();
            double y = pos.getY();

            double dragSourceX = 0;
            double dragSourceY = 0;
            double dragAnchorX = 0;
            double dragAnchorY = 0;

            DragSettings resizeSettings = new DragSettings(DragSettings.RESIZE);

            boolean resize = false;

            if (mX2 > topLeft.getX() - distance && mX2 < topLeft.getX() + distance && mY2 > topLeft.getY() - distance && mY2 < topLeft.getY() + distance) {
                resize = true;
                dragSourceX = x - width / 2;
                dragSourceY = -y - height / 2;
                dragAnchorX = x + width / 2;
                dragAnchorY = -y + height / 2;
            }
            if (mX2 > topRight.getX() - distance && mX2 < topRight.getX() + distance && mY2 > topRight.getY() - distance && mY2 < topRight.getY() + distance) {
                resize = true;
                dragSourceX = x + width / 2;
                dragSourceY = -y - height / 2;
                dragAnchorX = x - width / 2;
                dragAnchorY = -y + height / 2;
            }
            if (mX2 > bottomLeft.getX() - distance && mX2 < bottomLeft.getX() + distance && mY2 > bottomLeft.getY() - distance && mY2 < bottomLeft.getY() + distance) {
                resize = true;
                dragSourceX = x - width / 2;
                dragSourceY = -y + height / 2;
                dragAnchorX = x + width / 2;
                dragAnchorY = -y - height / 2;
            }
            if (mX2 > bottomRight.getX() - distance && mX2 < bottomRight.getX() + distance && mY2 > bottomRight.getY() - distance && mY2 < bottomRight.getY() + distance) {
                resize = true;
                dragSourceX = x + width / 2;
                dragSourceY = -y + height / 2;
                dragAnchorX = x - width / 2;
                dragAnchorY = -y - height / 2;
            }

            if (resize) {
                resizeSettings.setInitialSourceX(dragSourceX);
                resizeSettings.setInitialSourceY(dragSourceY);
                resizeSettings.setAnchorX(dragAnchorX);
                resizeSettings.setAnchorY(dragAnchorY);
                resizeSettings.setInitialScaleX(zoom);
                resizeSettings.setInitialScaleY(zoom);
                return resizeSettings;
            }
        }
        return new DragSettings(DragSettings.NONE);
    }

    @Override
    public void dragFromMouse(double mouseX, double mouseY, double dragSourceX, double dragSourceY){
        setAttribute("pos", (mouseX - dragSourceX) + "," + (dragSourceY - mouseY));
    }

    @Override
    public void resizeFromMouse(double mouseX, double mouseY, double resizeDragSourceX, double resizeDragSourceY, double resizeDragAnchorX, double resizeDragAnchorY){

        Point2D center = new Point2D((resizeDragSourceX + resizeDragAnchorX) / 2, (resizeDragSourceY + resizeDragAnchorY) / 2);

        double newWidth;
        double newHeight;

        newWidth = Math.abs(center.getX() - mouseX);
        newHeight = Math.abs(center.getY() - mouseY);

        double zoomW = Math.abs(center.getX() - resizeDragAnchorX) / newWidth;
        double zoomH = Math.abs(center.getY() - resizeDragAnchorY) / newHeight;

        setAttribute("zoom", Math.min(zoomW, zoomH) * Main.getDragSettings().getInitialScaleX());
    }
}
