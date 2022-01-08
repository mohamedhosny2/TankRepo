package Texture;



import Texture.Anim;
import com.sun.opengl.util.j2d.TextRenderer;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import static java.lang.System.exit;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.media.opengl.*;

import java.util.BitSet;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.glu.GLU;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class AnimGLEventListener3 extends AnimListener implements MouseListener {

    int timer = 0;
    String time = java.time.LocalTime.now() + "";
    GLCanvas glc;
    boolean isleft = false;
    boolean istop = true;
    boolean isright = false;
    boolean isdown = false;
    boolean isleft1 = false;
    boolean istop1 = false;
    boolean isright1 = false;
    boolean isdown1 = true;
    boolean exit = false;
    int s = 0;
    GLAutoDrawable gldddd;
    TextRenderer renderer = new TextRenderer(new Font("SanasSerif", Font.BOLD, 20));
    boolean visible1[] = {false, false, false};
    boolean visible[] = {false, false, false};
    int xlead[] = {0, 0, 0};
    int ylead[] = {0, 0, 0};

    int dir[] = {0, 0, 0};
    int vis = 0;
    int xlead1[] = {0, 0, 0};
    int ylead1[] = {0, 0, 0};

    int dir1[] = {0, 0, 0};
    int vis1 = 0;
    int xPosition = 90;
    int yPosition = 90;
    int animationIndex = 0;
    int maxWidth = 100;
    int maxHeight = 100;
    int x = 50, y = 0, xM = 90, yM = 20;
    int x1 = 0, y1 = 70;
    int[] xt = {18};
    int[] yt = {75};
    int[] xt1 = {40};
    int[] xt2 = {70};
    int[] yt1 = {85};
    int[] yt2 = {80};
    int ex = 0;
    int ex1 = 0;
    int ex2 = 0;
    int score = 0;
    int score1 = 0;
    int score2 = 0;
    int tankLives1 = 3;
    int tankLives2 = 3;
    int tankLives3 = 3;
    boolean how = false;
    boolean start = false, home = true, tank = false, tank1 = false, tank2 = false, player1 = false, player2 = false, multi = false, ispause = false;  //tank,tank1,tank2 are anims

    // Download enemy textures from https://craftpix.net/freebies/free-monster-2d-game-items/
    String textureNames[] = {"tank1.png", "tank1.png", "tank1.png", "tank1.png", "home.jpg", "bigstock-The-Word-Pause-Formed-By-Woode-260286709-1080x675.jpg",
        "fire.png", "3.png", "2.png", "1.png", "0.png", "Gun.png", "rock.jpg", "rock2.jpg", "33.png", "22.png",
        "11.png", "00.png", "bom1.png", "bom2.png", "bom3.png", "bom4.png", "bom5.png", "bom6.png", "bom7.png", "bom8.png", "back photo.jpeg", "how.png", "win.png", "bom9.png",
        "Gun_02.png", "tank2.png", "finalback2.png", "player-one-win.png", "player-second-win.png"};
    TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];
    int textures[] = new int[textureNames.length];

    /*
    ** set the way
     */
    public void setway(boolean top, boolean down, boolean right, boolean left) {
        this.istop = top;
        this.isdown = down;
        this.isright = right;
        this.isleft = left;

    }

    public void setway1(boolean top, boolean down, boolean right, boolean left) {
        this.istop1 = top;
        this.isdown1 = down;
        this.isright1 = right;
        this.isleft1 = left;

    }

    public void setGLCanvas(GLCanvas glc) {
        this.glc = glc;
    }

   
    public void init(GLAutoDrawable gld) {

        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);    //This Will Clear The Background Color To Black

        gl.glEnable(GL.GL_TEXTURE_2D);  // Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);
        try {
            FileInputStream music = new FileInputStream(new File("Back_3.wav"));
            AudioStream audios = new AudioStream(music);
            AudioPlayer.player.start(audios);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }

        for (int i = 0; i < textureNames.length; i++) {
            try {
                texture[i] = TextureReader.readTexture(assetsFolderName + "//" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);

//                mipmapsFromPNG(gl, new GLU(), texture[i]);
                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA, // Internal Texel Format,
                        texture[i].getWidth(), texture[i].getHeight(),
                        GL.GL_RGBA, // External format from image,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels() // Imagedata
                );
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
    }

    public void display(GLAutoDrawable gld) {
        timer++;
        gldddd = gld;
        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);       //Clear The Screen And The Depth Buffer
        gl.glLoadIdentity();

        DrawBackground(gl);
        renderer.beginRendering(gld.getWidth(), gld.getHeight());
        int total = score + score1 + score2;

        renderer.draw("score : " + total, 570, 640);

        renderer.endRendering();

        if (home) {
            DrawHome(gl);
        }
        if (start) {
            handleKeyPress();
            renderer.draw("TankLives1 : " + tankLives1, 0, 630);
            renderer.draw("TankLives2 : " + tankLives2, 200, 630);
            renderer.draw("TankLives3 : " + tankLives3, 400, 630);
      

            DrawSprite(gl, x, y, 0, 1); //the tank1--------
            this.moveBullet(gl, visible, xlead, ylead, dir); //الرصاص

            //map
            //--- Bricks
            DrawSprite3(gl, 80, 80, 12, 1);
            DrawSprite3(gl, 80, 70, 13, 1);
            DrawSprite3(gl, 80, 60, 12, 1);

            DrawSprite3(gl, 80, 30, 12, 1);
            DrawSprite3(gl, 80, 20, 13, 1);
            DrawSprite3(gl, 80, 10, 12, 1);

            DrawSprite3(gl, 50, 80, 12, 1);
            DrawSprite3(gl, 50, 70, 13, 1);
            DrawSprite3(gl, 50, 60, 12, 1);

            DrawSprite3(gl, 60, 40, 12, 1);
            DrawSprite3(gl, 60, 30, 13, 1);
            DrawSprite3(gl, 60, 20, 12, 1);

            DrawSprite3(gl, 30, 30, 12, 1);
            DrawSprite3(gl, 30, 40, 13, 1);
            DrawSprite3(gl, 30, 50, 12, 1);

            DrawSprite3(gl, 10, 50, 13, 1);
            DrawSprite3(gl, 10, 60, 13, 1);
            DrawSprite3(gl, 10, 70, 12, 1);
            DrawSprite3(gl, 10, 80, 12, 1);

            DrawSprite3(gl, 90, 0, textureNames.length - 9, 1); //back button

            if (tank) {
                if (ex == 8) {
                    if (score < 10) {
                        score += 10;
                    }

                } else {
                    ex = ex % 8;
                    DrawExplosion(gl, xt[0], yt[0], ex, 1);
                    ex++;
                }
                DrawSprite3(gl, xt[0], yt[0], textureNames.length - 6, 1);

            } else {

                DrawSprite3(gl, xt[0], yt[0], textureNames.length - 4, 1);
            }
            if (tank1) {

                if (ex1 == 8) {
                    if (score1 < 10) {
                        score1 += 10;

                    }

                } else {
                    ex1 = ex1 % 8;
                    DrawExplosion(gl, xt1[0], yt1[0], ex1, 1);
                    ex1++;
                }
                DrawSprite3(gl, xt1[0], yt1[0], textureNames.length - 6, 1); // الحفره

            } else {

                DrawSprite3(gl, xt1[0], yt1[0], textureNames.length - 4, 1);
                // this.moveBullet1(gl, visible1, xlead, ylead1, dir1);
            }
            try {
                DrawTime();
            } catch (ParseException ex) {
                Logger.getLogger(AnimGLEventListener3.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (tank2) {
                if (ex2 == 8) {
                    if (score2 < 10) {
                        score2 += 10;
                    }

                } else {

                    ex2 = ex2 % 8;
                    DrawExplosion(gl, xt2[0], yt2[0], ex2, 1);
                    ex2++;
                    System.out.println(ex1);
                }
                DrawSprite3(gl, xt2[0], yt2[0], textureNames.length - 6, 1);

            } else {

                DrawSprite3(gl, xt2[0], yt2[0], textureNames.length - 4, 1);
            }

            if (tank && tank1 && tank2) {
                DrawWin(gl, textureNames.length - 7);
            }
            if (ispause) {
                DrawPause(gl);
            }
        }

        if (multi) {
            handleKeyPress();

            this.moveBullet(gl, visible, xlead, ylead, dir);
            // ^^^^^^^^^^^^^^^^^^^^^ player 1 ^^^^^^^^^^^^^^^^^^^^^^^^^^
            DrawSprite(gl, x, y, 0, 1);

            //Bricks
            DrawSprite3(gl, 80, 80, 13, 1);
            DrawSprite3(gl, 80, 70, 13, 1);
            DrawSprite3(gl, 80, 60, 12, 1);

            DrawSprite3(gl, 80, 30, 12, 1);
            DrawSprite3(gl, 80, 20, 12, 1);
            DrawSprite3(gl, 80, 10, 13, 1);

            DrawSprite3(gl, 50, 80, 12, 1);
            DrawSprite3(gl, 50, 70, 13, 1);
            DrawSprite3(gl, 50, 60, 12, 1);

            DrawSprite3(gl, 60, 40, 12, 1);
            DrawSprite3(gl, 60, 30, 12, 1);
            DrawSprite3(gl, 60, 20, 12, 1);

            DrawSprite3(gl, 30, 30, 12, 1);
            DrawSprite3(gl, 30, 40, 12, 1);
            DrawSprite3(gl, 30, 50, 12, 1);

            DrawSprite3(gl, 10, 50, 13, 1);
            DrawSprite3(gl, 10, 60, 12, 1);
            DrawSprite3(gl, 10, 70, 12, 1);
            DrawSprite3(gl, 10, 80, 13, 1);

            DrawSprite3(gl, 90, 0, textureNames.length - 9, 1);
//            vis1 = vis1 % 4;
//            DrawVis1(gl);

            this.moveBullet1(gl, visible1, xlead1, ylead1, dir1);

            // ^^^^^^^^^^^^^^^^^^^^^ player 2 ^^^^^^^^^^^^^^^^^^^^^^^^^^
            DrawSprite2(gl, x1, y1, 0, 1);
            DrawSprite2(gl, x1, y1, 11, 1);

            if (player2) {
                if (ex == 8) {
                    DrawSprite3(gl, x1, y1, textureNames.length - 6, 1);
                    if (s < 10) {
                    }

                } else {

                    ex = ex % 8;
                    DrawExplosion(gl, x1, y1, ex, 1);
                    ex++;
                }
            }

            if (player1) {
                DrawSprite3(gl, x, y, textureNames.length - 6, 1);
                if (ex == 8) {
                    if (s < 10) {
                    }

                } else {
                    ex = ex % 8;
                    DrawExplosion(gl, x, y, ex, 1);
                    ex++;
                }

            }

            try {
                DrawTime();
            } catch (ParseException ex) {
                Logger.getLogger(AnimGLEventListener3.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (player1 && !player2) {
                DrawWin(gl, textureNames.length - 1);
            }
            if (!player1 && player2) {
                DrawWin(gl, textureNames.length - 2);
            }

            if (ispause) {
                DrawPause(gl);
            }

        }
        if (exit) {
            System.exit(0);
        }
        if (how) {
            DrawHow(gl);
        }

    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    public void DrawSprite(GL gl, int x, int y, int index, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * scale, 0.1 * scale, 1);

        //the move of the tank1----------------------
        if (isleft) {

            gl.glRotatef(90.0f, 0f, 0f, 1f);
        } else if (isright) {

            gl.glRotatef(-90.0f, 0f, 0f, 1f);
        } else if (istop) {
            gl.glRotatef(0.0f, 0f, 0f, 1f);

        } else {
            gl.glRotatef(180.0f, 0f, 0f, 1f);

        }

        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawExplosion(GL gl, int x, int y, int index, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index + 17]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * scale, 0.1 * scale, 1);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawSprite2(GL gl, int x, int y, int index, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * scale, 0.1 * scale, 1);
        if (isleft1) {

            gl.glRotatef(90.0f, 0f, 0f, 1f);
        } else if (isright1) {

            gl.glRotatef(-90.0f, 0f, 0f, 1f);
        } else if (istop1) {
            gl.glRotatef(0.0f, 0f, 0f, 1f);

        } else {
            gl.glRotatef(180.0f, 0f, 0f, 1f);

        }

        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawSprite3(GL gl, int x, int y, int index, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * scale, 0.1 * scale, 1);

        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawHome(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[textureNames.length - 3]);	// Turn Blending On

        gl.glPushMatrix();

        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawBackground(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[4]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);

        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);

        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    /*
    ** Draw pause 
     */
    public void DrawPause(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[5]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawWin(GL gl, int index) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawHow(GL gl) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[textureNames.length - 8]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawTime() throws ParseException {
        int sec1 = Integer.parseInt((java.time.LocalTime.now()).toString().substring(6, 8)),
                min1 = Integer.parseInt((java.time.LocalTime.now()).toString().substring(3, 5));
        String time1 = time;
        String time2 = java.time.LocalTime.now() + "";

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date1 = format.parse(time1);
        Date date2 = format.parse(time2);
        long difference = date2.getTime() - date1.getTime();

        String fi = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(difference),
                TimeUnit.MILLISECONDS.toSeconds(difference)
                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(difference))
        );

        renderer.beginRendering(gldddd.getWidth(), gldddd.getHeight());
        renderer.draw(fi, 600, 620);
        renderer.endRendering();
    }

    /*
    ** Draw Bullet 
     */
    public void DrawBullet(GL gl, int x, int y, int dir) {
        float scalex = 118f, scaley = 442f;
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[6]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.0001 * scaley, 0.0001 * scaley, 1);
        if (dir == -2) {

            gl.glRotatef(90.0f, 0f, 0f, 1f);
        } else if (dir == 2) {

            gl.glRotatef(-90.0f, 0f, 0f, 1f);
        } else if (dir == 1) {
            gl.glRotatef(0.0f, 0f, 0f, 1f);

        } else {
            gl.glRotatef(180.0f, 0f, 0f, 1f);

        }

        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawBullet1(GL gl, int x, int y, int dir) {
        float scalex = 118f, scaley = 442f;
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[6]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.0001 * scaley, 0.0001 * scaley, 1);
        if (dir == -2) {

            gl.glRotatef(90.0f, 0f, 0f, 1f);
        } else if (dir == 2) {

            gl.glRotatef(-90.0f, 0f, 0f, 1f);
        } else if (dir == 1) {
            gl.glRotatef(0.0f, 0f, 0f, 1f);

        } else {
            gl.glRotatef(180.0f, 0f, 0f, 1f);

        }

        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    /*
   >>>>>>>>>>>>>>>>>>>>>>>>>> move the Bullet <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
     */
    public void moveBullet(GL gl, boolean isvisiable[], int xLead[], int yLead[], int direction[]) {

        for (int i = 0; i < isvisiable.length; i++) {
            if (isvisiable[i] == true) {

                if (direction[i] == -2) {
                    DrawBullet(gl, xLead[i] = xLead[i] - 3, yLead[i], direction[i]);
                }
                if (direction[i] == 2) {
                    DrawBullet(gl, xLead[i] = xLead[i] + 3, yLead[i], direction[i]);
                }
                if (direction[i] == -1) {
                    DrawBullet(gl, xLead[i], yLead[i] = yLead[i] - 3, direction[i]);
                }
                if (direction[i] == 1) {
                    DrawBullet(gl, xLead[i], yLead[i] = yLead[i] + 3, direction[i]);
                }
                if (xLead[i] >= 100 || yLead[i] >= 100 || xLead[i] <= 0 || yLead[i] <= 0) {
                    visible[i] = false;
                    vis--;

                }

                for (int j = 0; j < xLead.length; j++) {
                    for (int k = 0; k < xt.length; k++) {
                        if ((xLead[j] >= xt[k] - 5 && xLead[j] <= xt[k] + 5) && (yLead[j] == 75)) {
                            if (tankLives1 > 0) {
                                tankLives1 -= 1;
                                tank = false;
                            } else {
                                tank = true;
                            }

                        }

                    }
                }
                for (int j = 0; j < xLead.length; j++) {
                    for (int k = 0; k < xt1.length; k++) {
                        if ((xLead[j] >= xt1[k] - 2 && xLead[j] <= xt1[k] + 4) && (yLead[j] == 80)) {
                            if (tankLives2 > 0) {
                                tankLives2 -= 1;
                                tank1 = false;
                            } else {
                                tank1 = true;
                            }

                        }

                    }
                }
                for (int j = 0; j < xLead.length; j++) {
                    for (int k = 0; k < xt2.length; k++) {
                        if ((xLead[j] >= xt2[k] - 2 && xLead[j] <= xt2[k] + 4) && (yLead[j] == 85)) {
                            if (tankLives3 > 0) {
                                tankLives3 -= 1;
                                tank2 = false;
                            } else {
                                tank2 = true;
                            }

                        }

                    }
                }
                //if (multi) {
                for (int j = 0; j < xLead.length; j++) {
                    for (int k = 0; k < xt2.length; k++) {
                        if (xLead[j] >= x1 - 4 && xLead[j] <= x1 + 4 && yLead[j] >= y1 - 3 && yLead[j] <= y1 + 3) {
                            player2 = true;

                        }
                    }
                }
                //}
            }

        }
    }

    public void moveBullet1(GL gl, boolean isvisiable[], int xLead[], int yLead[], int direction[]) {

        for (int i = 0; i < isvisiable.length; i++) {
            if (isvisiable[i] == true) {

                if (direction[i] == -2) {
                    DrawBullet1(gl, xLead[i] = xLead[i] - 3, yLead[i], direction[i]);
                }
                if (direction[i] == 2) {
                    DrawBullet1(gl, xLead[i] = xLead[i] + 3, yLead[i], direction[i]);
                }
                if (direction[i] == -1) {
                    DrawBullet1(gl, xLead[i], yLead[i] = yLead[i] - 3, direction[i]);
                }
                if (direction[i] == 1) {
                    DrawBullet1(gl, xLead[i], yLead[i] = yLead[i] + 3, direction[i]);
                }
                if (xLead[i] >= 100 || yLead[i] >= 100 || xLead[i] <= 0 || yLead[i] <= 0) {
                    visible1[i] = false;
                    vis1--;
//                    System.out.println("unvisible");
                }
                for (int j = 0; j < xLead.length; j++) {
                    for (int k = 0; k < xt.length; k++) {
                        if ((xLead[j] >= xt[k] - 5 && xLead[j] <= xt[k] + 2) && (yLead[j] == 75)) {
                            tank = true;

                        }

                    }
                }
                for (int j = 0; j < xLead.length; j++) {
                    for (int k = 0; k < xt1.length; k++) {
                        if ((xLead[j] >= xt1[k] - 5 && xLead[j] <= xt1[k] + 2) && (yLead[j] == 80)) {
                            tank1 = true;

                        }

                    }
                }
                for (int j = 0; j < xLead.length; j++) {
                    for (int k = 0; k < xt2.length; k++) {
                        if ((xLead[j] >= xt2[k] - 5 && xLead[j] <= xt2[k] + 5) && (yLead[j] == 85)) {
                            tank2 = true;

                        }

                    }
                }
                //if (multi) {

                for (int j = 0; j < xLead.length; j++) {
                    for (int k = 0; k < 1; k++) {
                        if (xLead[j] >= x - 4 && xLead[j] <= x + 4 && yLead[j] >= y - 3 && yLead[j] <= y + 3) {
                            player1 = true;

                        }
                    }
                }
                //}

            }

        }
    }

    public void setBullet(boolean visible[], int x[], int y[], int dir[]) {
        for (int i = 0; i < visible.length; i++) {
            if (visible[i] == false) {
                visible[i] = true;
                x[i] = this.x;
                y[i] = this.y;
                if (this.isleft) {
                    dir[i] = -2;
                }
                if (this.isright) {
                    dir[i] = 2;
                }
                if (this.istop) {
                    dir[i] = 1;
                }
                if (this.isdown) {
                    dir[i] = -1;
                }
                vis++;
                break;
            }
        }
    }

    public void setBullet1(boolean visible[], int x[], int y[], int dir[]) {
        for (int i = 0; i < visible1.length; i++) {
            if (visible1[i] == false) {
                visible1[i] = true;
                x[i] = this.x1;
                y[i] = this.y1;
                if (this.isleft1) {
                    dir[i] = -2;
                }
                if (this.isright1) {
                    dir[i] = 2;
                }
                if (this.istop1) {
                    dir[i] = 1;
                }
                if (this.isdown1) {
                    dir[i] = -1;
                }
                vis1++;
                break;
            }
        }
    }

    /*
     >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> KeyListener <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
     */
    public void handleKeyPress() {

        if (isKeyPressed(KeyEvent.VK_LEFT)) {
            if (((x >= 80 && x <= 90) && y >= 3 && y <= 38
                    || (x >= 80 && x <= 90) && y <= 87 && y >= 53)
                    || ((x >= 60 && x <= 70) && y >= 13 && y <= 47)
                    || ((x >= 50 && x <= 60) && y >= 53 && y <= 87)
                    || ((x >= 30 && x <= 40) && y >= 23 && y <= 57)
                    || ((x >= 10 && x <= 20) && y >= 43 && y <= 87)) {
            } else if (x > 0) {
                x--;
                setway(false, false, false, true);
            }
            animationIndex++;
        } else if (isKeyPressed(KeyEvent.VK_RIGHT)) {
            if (((x >= 73 && x <= 83) && y >= 3 && y <= 38
                    || (x >= 70 && x <= 80) && y <= 87 && y >= 53)
                    || ((x >= 50 && x <= 60) && y >= 13 && y <= 47)
                    || ((x >= 40 && x <= 50) && y >= 53 && y <= 87)
                    || ((x >= 20 && x <= 30) && y >= 23 && y <= 57)
                    || ((x >= 0 && x <= 10) && y >= 43 && y <= 87)) {
//     setway(true, true, true, false);
            } else if (x < maxWidth - 10) {
                x++;
                setway(false, false, true, false);

            }
            animationIndex++;
        } else if (isKeyPressed(KeyEvent.VK_DOWN)) {
            if (((x >= 79 && x <= 89) && y >= 81 && y <= 81
                    || (x >= 75 && x <= 89) && y == 31)
                    || ((x >= 55 && x <= 69) && y == 41)
                    || ((x >= 45 && x <= 59) && y == 81)
                    || ((x >= 25 && x <= 39) && y == 51)
                    || ((x >= 6 && x <= 19) && y == 81)) {
                setway(false, true, false, false);
            } else if (y > 0) {
                y--;
                setway(false, true, false, false);

            }
            animationIndex++;

        } else if (isKeyPressed(KeyEvent.VK_UP)) {
            if (((x >= 75 && x <= 89) && y >= 11 && y <= 11
                    || (x >= 75 && x <= 89) && y <= 51 && y >= 51)
                    || ((x >= 55 && x <= 69) && y >= 11 && y <= 11)
                    || ((x >= 45 && x <= 59) && y >= 51 && y <= 51)
                    || ((x >= 25 && x <= 39) && y >= 21 && y <= 21)
                    || ((x >= 6 && x <= 19) && y >= 41 && y <= 41)) {
                setway(true, false, false, false);
            } else if (y < maxHeight - 10) {
                y++;
                setway(true, false, false, false);
            }
            animationIndex++;
        }
        if (isKeyPressed(KeyEvent.VK_SPACE)) {
            this.setBullet(visible, this.xlead, this.ylead, this.dir);
        }
        if (isKeyPressed(KeyEvent.VK_A)) {
            if (((x1 >= 80 && x1 <= 90) && y1 >= 3 && y1 <= 38
                    || (x1 >= 80 && x1 <= 90) && y1 <= 87 && y1 >= 53)
                    || ((x1 >= 60 && x1 <= 70) && y1 >= 13 && y1 <= 47)
                    || ((x1 >= 50 && x1 <= 60) && y1 >= 53 && y1 <= 87)
                    || ((x1 >= 30 && x <= 40) && y1 >= 23 && y1 <= 57)
                    || ((x1 >= 10 && x1 <= 20) && y1 >= 43 && y1 <= 87)) {
//     setway(true, true, true, false);
            } else if (x1 > 0) {
                x1--;
                setway1(false, false, false, true);
            }
            animationIndex++;
        } else if (isKeyPressed(KeyEvent.VK_D)) {
            if (((x1 >= 73 && x1 <= 83) && y1 >= 3 && y1 <= 38
                    || (x1 >= 70 && x1 <= 80) && y1 <= 87 && y1 >= 53)
                    || ((x1 >= 50 && x1 <= 60) && y1 >= 13 && y1 <= 47)
                    || ((x1 >= 40 && x1 <= 50) && y1 >= 53 && y1 <= 87)
                    || ((x1 >= 20 && x1 <= 30) && y1 >= 23 && y1 <= 57)
                    || ((x1 >= 0 && x1 <= 10) && y1 >= 43 && y1 <= 87)) {
//     setway(true, true, true, false);
            } else if (x1 < maxWidth - 10) {
                x1++;
                setway1(false, false, true, false);

            }
            animationIndex++;
        } else if (isKeyPressed(KeyEvent.VK_S)) {
            if (y1 > 0) {
                y1--;
                setway1(false, true, false, false);

            }
        } else if (isKeyPressed(KeyEvent.VK_W)) {
            if (((x1 >= 73 && x1 <= 83) && y1 == 3
                    || (x1 >= 70 && x1 <= 80) && y1 == 53)
                    || ((x1 >= 50 && x1 <= 60) && y1 == 13)
                    || ((x1 >= 40 && x1 <= 50) && y1 == 53)
                    || ((x1 >= 20 && x1 <= 30) && y1 == 23)
                    || ((x1 >= 0 && x1 <= 10) && y == 43)) {
//     setway(true, true, true, false);
            } else if (y1 < maxHeight - 10) {
                y1++;
                setway1(true, false, false, false);
            }
            animationIndex++;
        }
        if (isKeyPressed(KeyEvent.VK_X)) {
            this.setBullet1(visible1, this.xlead1, this.ylead1, this.dir1);
        }

    }

    public BitSet keyBits = new BitSet(256);

    @Override
    public void keyPressed(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.set(keyCode);

        if (isKeyPressed(KeyEvent.VK_ESCAPE)) {
            if (!ispause) {
                ispause = !ispause;
                gldddd.repaint();
                Anim.animator.stop();

            } else {
                ispause = !ispause;
                gldddd.repaint();
                Anim.animator.start();

            }

        }
    }

    @Override
    public void keyReleased(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.clear(keyCode);
    }

    @Override
    public void keyTyped(final KeyEvent event) {
        // don't care 
    }

    public boolean isKeyPressed(final int keyCode) {
        return keyBits.get(keyCode);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        double x4 = e.getX();
        double y4 = e.getY();

        Component c = e.getComponent();
        double width = c.getWidth();
        double height = c.getHeight();
        /*get percent of GLCanvas instead of
        points and then converting it to our
        '100' based coordinate system. */
        xPosition = (int) ((x4 / width) * 100);
        yPosition = ((int) ((y4 / height) * 100));
        //reversing direction of y axis
        yPosition = 100 - yPosition;
        if (home) {
            if (xPosition <= 55 && xPosition >= 44 && yPosition <= 37 && yPosition >= 31) {
                start = true;
                home = false;
                multi = false;
            }
            if ((xPosition >= 45 && xPosition <= 54) && (yPosition >= 20 && yPosition <= 27)) {
                exit = true;
            }
            if ((xPosition >= 42 && xPosition <= 58) && (yPosition >= 41 && yPosition <= 46)) {
                multi = true;
                home = false;
            }
            if ((xPosition >= 90 && xPosition <= 94) && (yPosition >= 6 && yPosition <= 14)) {
                home = false;
                how = true;
                start = false;
            }

        }
        if (start) {
            if (xPosition <= 99 && xPosition >= 90 && yPosition <= 10 && yPosition >= 2) {
                start = false;
                home = true;
            }
        }
        if (multi) {
            if (xPosition <= 99 && xPosition >= 90 && yPosition <= 10 && yPosition >= 2) {
                multi = false;
                home = true;
            }
        }
        if (how) {
            if (xPosition <= 19 && xPosition >= 3 && yPosition <= 97 && yPosition >= 88) {
                how = false;
                home = true;
            }
        }

        System.out.println(xPosition + " " + yPosition);
        glc.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
