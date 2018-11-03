package apps.doxplsoftware.pl.projekt2018;

/**
 * Created by Dox on 2018-10-10.
 */

public class Block {
    private int x, y;
    private int sign_x, sign_y;
    boolean type;
    Block(int x, int y, boolean type)
    {
        this.x = x;
        this.y = y;
        this.type = type;
    }
    public boolean getBlockType()
    {
        return this.type;
    }
    public int getX()
    {
        return this.x;
    }
    public int getY()
    {
        return this.y;
    }
    public int getSignX()
    {
        return this.sign_x;
    }
    public int getSignY()
    {
        return this.sign_y;
    }
    public void setX(int x)
    {
        this.x = x;
    }
    public void setY(int y)
    {
        this.y = y;
    }
    public void setSignX(int sx)
    {
        this.sign_x = sx;
    }
    public void setSignY(int sy)
    {
        this.sign_y = sy;
    }
}
