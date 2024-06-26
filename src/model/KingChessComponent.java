package model;

import view.ChessboardPoint;
import controller.ClickController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;


public class KingChessComponent extends ChessComponent {
    /**
     * <br>
     * FIXME: 需要特别注意此处加载的图片是没有背景底色的！！！
     */
    private static Image KING_WHITE;
    private static Image KING_BLACK;
    private Image kingImage;
    private boolean kingCanCastle = true;//用于检查King是否移动过,默认为没有移动过，可以易位

    public boolean isKingCanCastle() {
        return kingCanCastle;
    }

    public void setKingCanCastle(boolean kingCanCastle) {
        this.kingCanCastle = kingCanCastle;
    }

    /**
     * 读取加载车棋子的图片
     *
     * @throws IOException
     */
    public void loadResource() throws IOException {
        if (theme == 0) {
            KING_WHITE = ImageIO.read(new File("./images/Chess.comTheme/king-white1.png"));//读取车的贴图信息
        }
        if (theme == 1) {
            KING_WHITE = ImageIO.read(new File("./images/DemoTheme/king-white.png"));
        }

        if (theme == 0) {
            KING_BLACK = ImageIO.read(new File("./images/Chess.comTheme/king-black1.png"));
        }
        if (theme == 1) {
            KING_BLACK = ImageIO.read(new File("./images/DemoTheme/king-black.png"));
        }
    }


    /**
     * 在构造棋子对象的时候，调用此方法以根据颜色确定rookImage的图片是哪一种
     *
     * @param color 棋子颜色
     */

    private void initiateKingImage(ChessColor color) {
        try {
            loadResource();
            if (color == ChessColor.WHITE) {
                kingImage = KING_WHITE;
            } else if (color == ChessColor.BLACK) {
                kingImage = KING_BLACK;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public KingChessComponent(ChessboardPoint chessboardPoint, Point location, ChessColor color, ClickController listener, int size, char name) {
        super(chessboardPoint, location, color, listener, size, name);
        initiateKingImage(color);
    }

    /**
     * @param chessComponents 棋盘
     * @param destination     目标位置，如(0, 0), (0, 7)等等
     * @return 车棋子移动的合法性
     */

    @Override
    //以下记录了king的行棋规则
    public boolean canMoveTo(ChessComponent[][] chessComponents, ChessboardPoint destination) {
        ChessboardPoint source = getChessboardPoint();  //用于记录该棋子当前位置
        if (source.getX() == destination.getX() && Math.abs(source.getY() - destination.getY()) == 1) {
            return true;
        } else if (source.getY() == destination.getY() && Math.abs(source.getX() - destination.getX()) == 1) {
            return true;
        } else if (source.getX() + source.getY() == destination.getX() + destination.getY() && Math.abs(source.getX() - destination.getX()) == 1 || source.getX() - source.getY() == destination.getX() - destination.getY() && Math.abs(source.getX() - destination.getX()) == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 注意这个方法，每当窗体受到了形状的变化，或者是通知要进行绘图的时候，就会调用这个方法进行画图。
     *
     * @param g 可以类比于画笔
     */
    @Override
    protected void paintComponent(Graphics g) {
        if (isSelected()) { // Highlights the model if selected.
            super.paintComponent(g);
            g.setColor(clicked);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            g.drawImage(kingImage, 0, 0, getWidth(), getHeight(), this);
        } else if (getCanBeCaptured()) {
            super.paintComponent(g);
            g.setColor(new Color(0, 0, 0, 30));
            g.fillOval(0, 0, getWidth(), getHeight());
            g.setColor(squareColor);
            g.fillOval(5, 5, 66, 66);
            g.drawImage(kingImage, 0, 0, getWidth(), getHeight(), this);
        } else if (isMoving) {
            //如果棋子在移动，就不用绘制棋盘格
            g.drawImage(kingImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            super.paintComponent(g);
            g.drawImage(kingImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
