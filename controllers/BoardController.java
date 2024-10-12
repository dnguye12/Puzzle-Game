package controllers;

import models.BoardModel;
import models.Cell;
import views.BoardView;
import views.Menubar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BoardController {
    private BoardModel model;
    private BoardView view;
    private AnimationController animationController;
    private Timer timer;
    private boolean isDragging;
    private boolean keyboardMode;
    private int keyboardCol;
    private int keyboardRow;

    public BoardController(String imagePath, Menubar.Difficulty difficulty) {
        this.model = new BoardModel(imagePath, difficulty);
        this.view = new BoardView(this);
        this.animationController = new AnimationController(this.view);
        this.timer = new Timer(0, null);
        this.view.setAnimationController(this.animationController);
        this.isDragging = false;
        this.keyboardMode = false;
        this.keyboardCol = 0;
        this.keyboardRow = 0;
        this.setupListeners();
    }

    public BoardView getView() {
        return this.view;
    }

    public BoardModel getModel() {
        return this.model;
    }

    public boolean isKeyboardMode() {
        return keyboardMode;
    }

    public int getKeyboardCol() {
        return keyboardCol;
    }

    public int getKeyboardRow() {
        return keyboardRow;
    }

    public int selectKeyboard() {
        return this.model.getCellWidthCount() * this.keyboardRow + this.keyboardCol;
    }

    private void resetTimer() {
        for (ActionListener listener : timer.getActionListeners()) {
            timer.removeActionListener(listener);
        }
    }
    private void checkEnd() {
        if(this.model.endGame()) {
            System.out.println("end");
        }
    }

    private void setupListeners() {
        this.view.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                Cell selectedCell = model.getSelectedCell();
                if (!animationController.isAnimating()) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        resetTimer();
                        if (selectedCell == null) {
                            Cell helper = model.clickOnCell(e.getPoint());
                            if (helper != null) {
                                timer.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        if (!animationController.isScaling()) {
                                            helper.setSelected(true);
                                            model.setSelectedCell(helper);
                                            animationController.setScalingIdx(helper.getIdx());
                                            animationController.startScaling(true);
                                            timer.stop();
                                            view.revalidate();
                                            view.repaint();
                                        }
                                    }
                                });
                            }
                        } else {
                            Cell other = model.clickOnCell(e.getPoint());
                            if (other != null) {

                                if (!other.isSelected()) {
                                    animationController.startScaling(false);
                                    timer.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            if (!animationController.isScaling()) {
                                                selectedCell.setSelected(false);
                                                other.setSelected(true);
                                                model.setSelectedCell(other);
                                                animationController.setScalingIdx(other.getIdx());
                                                animationController.startScaling(true);
                                                timer.stop();
                                                view.revalidate();
                                                view.repaint();
                                            }
                                        }
                                    });
                                }
                            }
                        }
                        timer.start();
                    }

                    if (e.getButton() == MouseEvent.BUTTON2 && selectedCell != null) {
                        Point helper = selectedCell.getPos();
                        helper = new Point(helper);
                        helper.x += selectedCell.getImage().getWidth(null) / 2;
                        helper.y += selectedCell.getImage().getHeight(null) / 2;
                        view.setDonutMenuPos(helper);
                        view.toggleShowingDonutMenu();
                        view.revalidate();
                        view.repaint();
                    }

                    if (e.getButton() == MouseEvent.BUTTON3 && selectedCell != null) {
                        Cell other = model.clickOnCell(e.getPoint());
                        if (other != null) {
                            resetTimer();
                            if (other.isSelected()) {
                                animationController.startScaling(false);
                                timer.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        if (!animationController.isScaling()) {
                                            selectedCell.setSelected(false);
                                            model.setSelectedCell(null);
                                            model.setDraggedCell(null);
                                            animationController.setScalingIdx(-1);
                                            timer.stop();
                                            view.revalidate();
                                            view.repaint();
                                        }
                                    }
                                });
                                timer.start();
                            } else {
                                animationController.setSwapingCells(selectedCell, other);
                                animationController.startSwaping();

                                timer.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        if (!animationController.isSwaping()) {
                                            selectedCell.setSelected(false);
                                            model.replaceCell(selectedCell, other);
                                            model.setSelectedCell(null);
                                            model.setDraggedCell(null);
                                            animationController.setScalingIdx(-1);
                                            timer.stop();
                                            view.revalidate();
                                            view.repaint();
                                            checkEnd();
                                        }
                                    }
                                });
                                timer.start();
                            }
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!animationController.isAnimating()) {
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        if (isDragging) {
                            resetTimer();
                            isDragging = false;
                            Cell draggedCell = model.getSelectedCell();
                            Cell targetedCell = model.clickOnCell(e.getPoint());
                            if (targetedCell != null) {
                                model.replaceCell(draggedCell, targetedCell);
                                Point helper = new Point(draggedCell.getPos());
                                draggedCell.setPos(targetedCell.getPos());
                                targetedCell.setPos(helper);
                            }
                            draggedCell.setDragged(false);
                            draggedCell.setSelected(false);
                            model.setDraggedCell(null);
                            model.setSelectedCell(null);
                            animationController.setScalingIdx(-1);
                            view.revalidate();
                            view.repaint();
                            checkEnd();
                        }
                    }
                }
                if (e.getButton() != MouseEvent.BUTTON2) {
                    if (view.isShowingDonutMenu()) {
                        view.setShowingDonutMenu(false);
                        view.revalidate();
                        view.repaint();
                    }
                }
            }
        });

        this.view.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!animationController.isAnimating() && SwingUtilities.isLeftMouseButton(e)) {
                    Point helper = e.getPoint();
                    resetTimer();
                    if (!isDragging) {
                        Cell clickedCell = model.clickOnCell(helper);
                        if (clickedCell != null && clickedCell.isSelected()) {
                            Cell draggedCell = new Cell(clickedCell);
                            Image img = draggedCell.getImage();
                            draggedCell.setDragged(true);
                            draggedCell.setPos(new Point(helper.x - img.getWidth(null) / 2, helper.y - img.getHeight(null) / 2));
                            model.setDraggedCell(draggedCell);
                            isDragging = true;
                        }
                    } else {
                        Cell draggedCell = model.getDraggedCell();
                        Image img = draggedCell.getImage();
                        draggedCell.setPos(new Point(helper.x - img.getWidth(null) / 2, helper.y - img.getHeight(null) / 2));
                        model.setDraggedCell(draggedCell);
                    }
                    view.revalidate();
                    view.repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (view.isShowingDonutMenu()) {
                    int helper = view.getHoveredDonutSection(e.getPoint());
                    view.setHoveredSection(helper);
                    Cell selectedCell = model.getSelectedCell();
                    if (helper != -1 && selectedCell != null) {
                        Cell.Rotation helperRotation = Cell.Rotation.intToRotation(helper);
                        if (helperRotation != null) {
                            selectedCell.setRotation(helperRotation);
                        }
                    }
                    view.revalidate();
                    view.repaint();
                    checkEnd();
                }
            }
        });

        this.view.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (!isDragging && !animationController.isAnimating()) {
                    Cell selectedCell = model.getSelectedCell();
                    if (selectedCell != null) {
                        int notch = e.getWheelRotation();
                        resetTimer();
                        if (notch < 0) {
                            animationController.startRotating(selectedCell, false);
                            timer.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if (!animationController.isRotating()) {
                                        selectedCell.setRotation(selectedCell.getRotation().back());
                                        timer.stop();
                                        view.revalidate();
                                        view.repaint();
                                        checkEnd();
                                    }
                                }
                            });
                            timer.start();
                        } else {
                            animationController.startRotating(selectedCell, true);
                            timer.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if (!animationController.isRotating()) {
                                        selectedCell.setRotation(selectedCell.getRotation().next());
                                        timer.stop();
                                        view.revalidate();
                                        view.repaint();
                                        checkEnd();
                                    }
                                }
                            });
                            timer.start();
                        }
                    }
                }
            }
        });

        this.view.addKeyListener(new KeyListener() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    keyboardMode = !keyboardMode;
                }

                if (keyboardMode) {
                    if (e.getKeyCode() == KeyEvent.VK_UP) {
                        if (keyboardCol > 0) {
                            keyboardCol--;
                        }
                    }
                    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        if (keyboardCol < model.getCellHeightCount() - 1) {
                            keyboardCol++;
                        }
                    }
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        if (keyboardRow > 0) {
                            keyboardRow--;
                        }
                    }
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        if (keyboardRow < model.getCellWidthCount() - 1) {
                            keyboardRow++;
                        }
                    }
                    if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                        if(!animationController.isAnimating()) {
                            int idx = selectKeyboard();
                            Cell selectedCell = model.getSelectedCell();
                            Cell other = model.getCells().get(idx);
                            resetTimer();
                            if (selectedCell != null) {
                                if(other.isSelected()) {
                                    animationController.startScaling(false);
                                    timer.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            if (!animationController.isScaling()) {
                                                selectedCell.setSelected(false);
                                                model.setSelectedCell(null);
                                                model.setDraggedCell(null);
                                                animationController.setScalingIdx(-1);
                                                timer.stop();
                                                view.revalidate();
                                                view.repaint();
                                            }
                                        }
                                    });
                                }else {
                                    animationController.setSwapingCells(selectedCell, other);
                                    animationController.startSwaping();

                                    timer.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            if(!animationController.isSwaping()) {
                                                selectedCell.setSelected(false);
                                                model.replaceCell(selectedCell, other);
                                                model.setSelectedCell(null);
                                                model.setDraggedCell(null);
                                                animationController.setScalingIdx(-1);
                                                timer.stop();
                                                view.revalidate();
                                                view.repaint();
                                                checkEnd();
                                            }
                                        }
                                    });
                                }
                            } else {
                                timer.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        if(!animationController.isScaling()) {
                                            other.setSelected(true);
                                            model.setSelectedCell(other);
                                            animationController.setScalingIdx(idx);
                                            animationController.startScaling(true);
                                            timer.stop();
                                            view.revalidate();
                                            view.repaint();
                                        }
                                    }
                                });
                            }
                            timer.start();
                        }
                    }
                    if(e.getKeyCode() == KeyEvent.VK_E) {
                        int idx = selectKeyboard();
                        Cell other = model.getCells().get(idx);
                        if(other.isSelected()) {
                            resetTimer();
                            animationController.startRotating(other, true);
                            timer.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if (!animationController.isRotating()) {
                                        other.setRotation(other.getRotation().next());
                                        timer.stop();
                                        view.revalidate();
                                        view.repaint();
                                        checkEnd();
                                    }
                                }
                            });
                            timer.start();
                        }
                    }
                    if(e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_Q) {
                        int idx = selectKeyboard();
                        Cell other = model.getCells().get(idx);
                        if(other.isSelected()) {
                            resetTimer();
                            animationController.startRotating(other, false);
                            timer.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    if (!animationController.isRotating()) {
                                        other.setRotation(other.getRotation().back());
                                        timer.stop();
                                        view.revalidate();
                                        view.repaint();
                                        checkEnd();
                                    }
                                }
                            });
                            timer.start();
                        }
                    }
                }
                view.revalidate();
                view.repaint();
            }

            @Override
            public void keyPressed(KeyEvent e) {


            }

            @Override
            public void keyTyped(KeyEvent e) {

            }

        });
    }
}
