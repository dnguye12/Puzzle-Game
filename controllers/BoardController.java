package controllers;

import models.BoardModel;
import models.Cell;
import views.BoardView;

import java.awt.*;
import java.awt.event.*;

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
                if(e.getButton() == MouseEvent.BUTTON1) {
                    if(selectedCell == null) {
                        Cell helper = model.clickOnCell(e.getPoint());
                        if(helper != null) {
                            helper.setSelected(true);
                            model.setSelectedCell(helper);
                        }
                    }else {
                        selectedCell.setSelected(false);
                        Cell other = model.clickOnCell(e.getPoint());
                        if (other != null) {
                            other.setSelected(true);
                            model.setSelectedCell(other);
                        }
                    }
                    view.revalidate();
                    view.repaint();
                }

                if(e.getButton() == MouseEvent.BUTTON2 && selectedCell != null) {
                    Point helper = selectedCell.getPos();
                    helper = new Point(helper);
                    helper.x += selectedCell.getImage().getWidth(null) / 2;
                    helper.y += selectedCell.getImage().getHeight(null) / 2;
                    view.setDonutMenuPos(helper);
                    view.toggleShowingDonutMenu();
                    view.revalidate();
                    view.repaint();
                }


                if(e.getButton() == MouseEvent.BUTTON3 && selectedCell != null) {
                    Cell other = model.clickOnCell(e.getPoint());
                    if(other != null) {
                        selectedCell.setSelected(false);
                        model.replaceCell(selectedCell, other);
                        model.setSelectedCell(null);
                        view.revalidate();
                        view.repaint();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON1) {
                    if(isDragging) {
                        isDragging = false;
                        Cell draggedCell = model.getSelectedCell();
                        draggedCell.setDragged(false);
                        draggedCell.setSelected(false);
                        Cell targetedCell = model.clickOnCell(e.getPoint());
                        if(targetedCell != null) {
                            model.replaceCell(draggedCell, targetedCell);
                        }
                        model.setDraggedCell(null);
                        model.setSelectedCell(null);
                        view.revalidate();
                        view.repaint();
                    }
                }
            }
        });

        this.view.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    Point helper = e.getPoint();
                    if (!isDragging) {
                        Cell clickedCell = model.clickOnCell(helper);
                        if (clickedCell != null && clickedCell.isSelected()) {
                            Cell draggedCell = new Cell(clickedCell);
                            draggedCell.setDragged(true);
                            draggedCell.setPos(new Point(helper.x + OFFSET, helper.y + OFFSET));
                            model.setDraggedCell(draggedCell);
                            isDragging = true;
                        }
                    } else {
                        Cell draggedCell = model.getDraggedCell();
                        draggedCell.setPos(new Point(helper.x + OFFSET, helper.y + OFFSET));
                        model.setDraggedCell(draggedCell);
                    }
                    view.revalidate();
                    view.repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if(view.isShowingDonutMenu()) {
                    int helper = view.getHoveredDonutSection(e.getPoint());
                    view.setHoveredSection(helper);
                    Cell selectedCell = model.getSelectedCell();
                    if(helper != -1 && selectedCell != null) {
                        Cell.Rotation helperRotation = Cell.Rotation.intToRotation(helper);
                        if(helperRotation != null) {
                            selectedCell.setRotation(helperRotation);
                        }
                    }
                    view.revalidate();
                    view.repaint();
                }
            }
        });

        this.view.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                Cell selectedCell = model.getSelectedCell();
                if(selectedCell != null) {
                    int notch = e.getWheelRotation();
                    if(notch < 0) {
                        selectedCell.setRotation(selectedCell.getRotation().back());
                    }else {
                        selectedCell.setRotation(selectedCell.getRotation().next());
                    }
                    view.revalidate();
                    view.repaint();
                }
            }
        });
    }
}
