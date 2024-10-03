package controllers;

import models.BoardModel;
import models.Cell;
import views.BoardView;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class BoardController {
    private final int OFFSET = 5;
    private BoardModel model;
    private BoardView view;
    private boolean isDragging;

    public BoardController(String imagePath) {
        this.model = new BoardModel(imagePath);
        this.view = new BoardView(this.model);
        this.isDragging = false;
        this.setupListeners();
    }

    public BoardView getView() {
        return this.view;
    }

    private void setupListeners() {
        this.view.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                Cell selectedCell = model.getSelectedCell();
                if(selectedCell != null && e.getButton() == MouseEvent.BUTTON3) {
                    selectedCell.setRotation(selectedCell.getRotation().next());
                    Cell draggedCell = model.getDraggedCell();
                    if(draggedCell != null) {
                        draggedCell.setRotation(draggedCell.getRotation().next());
                    }
                    view.revalidate();
                    view.repaint();
                }
                if(e.getButton() == MouseEvent.BUTTON2) {
                    view.setDonutMenuPos(e.getPoint());
                    view.toggleShowingDonutMenu();
                    view.revalidate();
                    view.repaint();
                }
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1) {
                    Cell selectedCell = model.getSelectedCell();
                    if (selectedCell != null) {
                        selectedCell.setSelected(false);
                        Cell other = model.clickOnCell(e.getPoint());
                        if (other != null) {
                            model.replaceCell(selectedCell, other);
                        }
                    }
                    model.setSelectedCell(null);
                    model.setDraggedCell(null);
                    isDragging = false;
                    view.revalidate();
                    view.repaint();

                    if (model.endGame()) {
                        System.out.println("end");
                    }
                }
            }
        });

        this.view.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point helper = e.getPoint();
                if (isDragging) {
                    if (model.getSelectedCell() != null) {
                        Cell draggedCell = model.getDraggedCell();
                        draggedCell.setPos(new Point(helper.x + OFFSET, helper.y + OFFSET));
                        model.setDraggedCell(draggedCell);

                        view.revalidate();
                        view.repaint();
                    }
                } else {
                    Cell selectedCell = model.clickOnCell(helper);
                    if (selectedCell != null) {
                        selectedCell.setSelected(true);
                        Cell draggedCell = new Cell(selectedCell);
                        draggedCell.setPos(new Point(helper.x - OFFSET, helper.y - OFFSET));
                        draggedCell.setRotation(selectedCell.getRotation());
                        model.setSelectedCell(selectedCell);
                        model.setDraggedCell(draggedCell);
                        isDragging = true;
                        view.revalidate();
                        view.repaint();
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if(view.isShowingDonutMenu()) {
                    view.setHoveredSection(view.getHoveredDonutSection(e.getPoint()));
                    view.revalidate();
                    view.repaint();
                }
            }
        });
    }
}
