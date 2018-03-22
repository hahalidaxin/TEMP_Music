package com.bn.constant;

import java.util.ArrayList;
import java.util.List;

public class SourceConstant {
    public static float loadPosition=90;
    public static float Angle2D=0;
    public static float ColorCS=0;
    
    public static float BackTextx=540f;
    public static float BackTexty=610f;
    public static float BackText_Sizex=700f;
    public static float BackText_Sizey=700f;
    
    public static float step=0;
    public static float AngleSpng=0;
    public static int spngId;
    public static float spngx=540f;
    public static float spngy=960f;
    public static float spng_Sizex=1080f;
    public static float spng_Sizey=1920f;


    public static int SpecialBZ=0;

    
    
    public static boolean musicOff = false;
    public static boolean effictOff = false;

    public static int SOUND_Click=1;
    public static int SOUND_Back=2;
    public static int SOUND_DropMoney=3;
    public static float speed=0;
    public static boolean isBGMusic=false;

    
    public static float screenWidth = 0;
    public static float screenHeight = 0;
    public static float r=8f;	
    public final static float UNIT_SIZE=0.5f;
    public final static float TIME_STEP=1.0f/60;
    public final static int MAX_SUB_STEPS=2;
    public static float ANGLE_MIN=-90;
    public static float ANGLE_MAX=90;
    public 	static float EYE_X=0;
    public  static float EYE_Y=4;
    public  static float EYE_Z=22;

    public final static float TARGET_X=0;
    public final static float TARGET_Y=4;
    public final static float TARGET_Z=14;

    public static float scalebl=0.3f;
    public static float scaleblclaw=0.278f;

    public static float ganTLength=21.968f*scalebl;
    public static float ganULength=20.246f*scalebl;
    public static float ganLLength=1.772f*scalebl;
    public static float ganURadius=0.35f*scalebl;
    public static float ganLRadius=0.7f*scalebl;

    public static float claw1th=2.381f*scaleblclaw;
    public static float claw2th=4.216f*scaleblclaw;
    public static float claw3th=2.126f*scaleblclaw;

    public static float clawtzx=4.796f*scaleblclaw;
    public static float clawRadius=0.172f*scaleblclaw;

    public static float clawAngle1=32.07f;
    public static float clawAngle2=44.0f;
    public static float clawAngle3=30.0f;
    
    public static boolean isSet=false;
    public static boolean isYXJXTouch=false;
    
    public static float Gamequitx=200f;
    public static float Gamequity=1680f;
    public static float Gamequit_SIZEx=410f;
    public static float Gamequit_SIZEy=210f;
    public static float Gamequit_TOUCH_LEFT_x=Gamequitx-Gamequit_SIZEx/2;
    public static float Gamequit_TOUCH_RIGHT_x=Gamequitx+Gamequit_SIZEx/2;
    public static float Gamequit_TOUCH_BOTTOM_y=Gamequity+Gamequit_SIZEy/2;
    public static float Gamequit_TOUCH_TOP_y=Gamequity-Gamequit_SIZEy/2;
    

    

    public static float GameScorex=200f;
    public static float GameScorey=1280f;
    public static float GameScore_SIZEx=410f;
    public static float GameScore_SIZEy=210f;
    public static float GameScore_TOUCH_LEFT_x=GameScorex-GameScore_SIZEx/2;
    public static float GameScore_TOUCH_RIGHT_x=GameScorex+GameScore_SIZEx/2;
    public static float GameScore_TOUCH_BOTTOM_y=GameScorey+GameScore_SIZEy/2;
    public static float GameScore_TOUCH_TOP_y=GameScorey-GameScore_SIZEy/2;
    

    public static int GameAboutId;
    public static int GameAboutDownId;
    public static float GameAboutx=200f;
    public static float GameAbouty=1480f;
    public static float GameAbout_SIZEx=410f;
    public static float GameAbout_SIZEy=210f;
    public static float GameAbout_TOUCH_LEFT_x=GameAboutx-GameAbout_SIZEx/2;
    public static float GameAbout_TOUCH_RIGHT_x=GameAboutx+GameAbout_SIZEx/2;
    public static float GameAbout_TOUCH_BOTTOM_y=GameAbouty+GameAbout_SIZEy/2;
    public static float GameAbout_TOUCH_TOP_y=GameAbouty-GameAbout_SIZEy/2;

    

    public static int GameAboutTextId;
    public static float GameAboutTextx=540f;
    public static float GameAboutTexty=960f;
    public static float GameAboutText_SIZEx=1000f;
    public static float GameAboutText_SIZEy=450f;

    
    public static int MainViewSCId;
    public static int MainViewSCDownId;
    public static float MainViewSCx=890f;
    public static float MainViewSCy=1460f;
    public static float MainViewSCSIZEx=390f;
    public static float MainViewSCSIZEy=150f;
    public static float MainViewSC_TOUCH_LEFT_x=MainViewSCx-MainViewSCSIZEx/2;
    public static float MainViewSC_TOUCH_RIGHT_x=MainViewSCx+MainViewSCSIZEx/2;
    public static float MainViewSC_TOUCH_BOTTOM_y=MainViewSCy+MainViewSCSIZEy/2;
    public static float MainViewSC_TOUCH_TOP_y=MainViewSCy-MainViewSCSIZEy/2;



    public static int MainView_BGId;
    public static int MainView_SGMId;
    public static int MainView_SGMDId;
    public static int MainView_JPSCDId;
    public static int MainView_JPSCId;

    public static int MainView_YXJXId;
    public static int MainView_YXJXDId;

    
    public static float YXJX_x=880;
    public static float YXJX_y=1280;
    public static float YXJX_SIZE_x=400;
    public static float YXJX_SIZE_y=200;
    public static float YXJX_TOUCH_LEFT_x=YXJX_x-YXJX_SIZE_x/2;
    public static float YXJX_TOUCH_RIGHT_x=YXJX_x+YXJX_SIZE_x/2;
    public static float YXJX_TOUCH_BOTTOM_y=YXJX_y+YXJX_SIZE_y/2;
    public static float YXJX_TOUCH_TOP_y=YXJX_y-YXJX_SIZE_y/2;
    
    public static float MainView_BG_x=540;
    public static float MainView_BG_y=960;
    public static float MainView_BG_SIZE_x=1080;
    public static float MainView_BG_SIZE_y=1920;
    
    public static float StartGame_x=880;
    public static float StartGame_y=1080;
    public static float StartGame_SIZE_x=400;
    public static float StartGame_SIZE_y=200;
    public static float StartGame_TOUCH_LEFT_x=StartGame_x-StartGame_SIZE_x/2;
    public static float StartGame_TOUCH_RIGHT_x=StartGame_x+StartGame_SIZE_x/2;
    public static float StartGame_TOUCH_BOTTOM_y=StartGame_y+StartGame_SIZE_y/2;
    public static float StartGame_TOUCH_TOP_y=StartGame_y-StartGame_SIZE_y/2;

    
    public static int GameSDId;
    public static int GameSDDownId;
    public static float GameSDx=880f;
    public static float GameSDy=1680f;
    public static float GameSD_SIZEx=410f;
    public static float GameSD_SIZEy=210f;
    public static float GameSD_TOUCH_LEFT_x=GameSDx-GameSD_SIZEx/2;
    public static float GameSD_TOUCH_RIGHT_x=GameSDx+GameSD_SIZEx/2;
    public static float GameSD_TOUCH_BOTTOM_y=GameSDy+GameSD_SIZEy/2;
    public static float GameSD_TOUCH_TOP_y=GameSDy-GameSD_SIZEy/2;

    public static int AllbackId;
    public static float YXJXBackx=130f;
    public static float YXJXBacky=1800f;
    public static float YXJXBackSizex=250f;
    public static float YXJXBackSizey=250f;
    public static float YXJXBack_TOUCH_LEFT_x=YXJXBackx-YXJXBackSizex/2;
    public static float YXJXBack_TOUCH_RIGHT_x=YXJXBackx+YXJXBackSizex/2;
    public static float YXJXBack_TOUCH_BOTTOM_y=YXJXBacky+YXJXBackSizey/2;
    public static float YXJXBack_TOUCH_TOP_y=YXJXBacky-YXJXBackSizey/2;

    public static float StepYXJS=2f;

    public static int Page0Id;
    public static int Page1Id;
    public static int Page2Id;
    public static int Page3Id;
    public static int Page4Id;

    public static float[] Pagex=new float[]{540f,1620f,2700f,3780f,4860f};

    public static float Pagey=960f;
    public static float PageSizex=1080f;
    public static float PageSizey=1920f;
    

    


    public static int jbId;
    public static int floorTextureId;
    public static int TextureId;
    public static int clawId;
    public static int ganId;
    public static int dunId;
    public static int doll1Id;
    public static int doll2Id;
    public static int doll0Id;
    public static int holeId;
    public static int dollboxId;
    public static int babeId;
    public static int holeboxId;
    public static int niuId;
    public static int tvId;
    public static int RigidBodyId;
    public static int ganboxId;
    public static int HBId;

    
    public static int Box1Id;
    public static float Box_x=540;
    public static float Box_y=1800;
    public static float Box_SIZE_x=1120;
    public static float Box_SIZE_y=320;

    public static int Box2Id;
    public static float Box2_x=540;
    public static float Box2_y=1590;
    public static float Box2_SIZE_x=1120;
    public static float Box2_SIZE_y=100;
    
    public static float robotX=-0.5f;
    public static float robotY=2.5f;
    public static float robotZ=14.0f;

    public static int robotId;

    public static float robotbl=3.2f;

    public static float robotfootx=0.04f*robotbl;
    public static float robotfooty=0.014f*robotbl;
    public static float robotfootz=0.067f*robotbl;

    public static float robottuix=0.032f*robotbl;
    public static float robottuiy=0.042f*robotbl;
    public static float robottuiz=0.051f*robotbl;

    public static float robotbodyx=0.084f*robotbl;
    public static float robotbodyy=0.090f*robotbl;
    public static float robotbodyz=0.067f*robotbl;

    public static float robottopx=0.063f*robotbl;
    public static float robottopy=0.045f*robotbl;
    public static float robottopz=0.056f*robotbl;

    public static float robothand1x=0.0105f*robotbl;
    public static float robothand1y=0.09f*robotbl;
    public static float robothand1z=0.043f*robotbl;

    public static float robothand2x=0.0155f*robotbl;
    public static float robothand2y=0.040f*robotbl;
    public static float robothand2z=0.045f*robotbl;
    
    
    public static int CarId;

    public static float CarX=0.0f;
    public static float CarY=2.5f;
    public static float CarZ=13.5f;

    public static float Carbl=4.0f;
    public static float CarR=0.135f*Carbl;

    public static float Carbuttomx=0.145f*Carbl;
    public static float Carbuttomy=0.0625f*Carbl;
    public static float Carbuttomz=0.1960f*Carbl;

    public static float Carfootx=0.013f*Carbl; 
    public static float Carfooty=0.029f*Carbl; 
    public static float Carfootz=0.0315f*Carbl;
    
    
    public static int CameraId;

    public static float CameraX=-1.0f;
    public static float CameraY=2.5f;
    public static float CameraZ=13.5f;

    public static float Camerabl=3f;
    public static float Camerabodyx=0.22f*Camerabl;
    public static float Camerabodyy=0.12f*Camerabl;
    public static float Camerabodyz=0.060f*Camerabl;

    public static float CameraR=0.105f*Camerabl;
    public static float CameraH=0.165f*Camerabl;

    public static float CameraTopx=0.10f*Camerabl;
    public static float CameraTopy=0.030f*Camerabl;
    public static float CameraTopz=0.065f*Camerabl;
    
    public static int parrotId;

    public static float ParrotBL=3f;

    public static float ParrotX=1.0f;
    public static float ParrotY=2.5f;
    public static float ParrotZ=12.0f;

    public static float Parrotx=0.15f*ParrotBL;
    public static float Parroty=0.175f*ParrotBL;
    public static float Parrotz=0.14f*ParrotBL;

    public static float ParrotFootx=0.03f*ParrotBL;
    public static float ParrotFooty=0.04f*ParrotBL;
    public static float ParrotFootz=0.01f*ParrotBL;
    
    
    public static int  MGstartId;
    public static int  MGstartDownId;
    public static float MGstart_x=500;
    public static float MGstart_y=1660;
    public static float MGstart_SIZE_x=280;
    public static float MGstart_SIZE_y=230;
    public static float MGstart_TOUCH_LEFT_x=MGstart_x-MGstart_SIZE_x/2;
    public static float MGstart_TOUCH_RIGHT_x=MGstart_x+MGstart_SIZE_x/2;
    public static float MGstart_TOUCH_BOTTOM_y=MGstart_y+MGstart_SIZE_y/2;
    public static float MGstart_TOUCH_TOP_y=MGstart_y-MGstart_SIZE_y/2;
    


    public static int shuaxinId;
    public static int shuaxinDownId;
    public static float shuaxin_x=800;
    public static float shuaxin_y=1660;
    public static float shuaxin_SIZE_x=280;
    public static float shuaxin_SIZE_y=230;
    public static float shuaxin_TOUCH_LEFT_x=shuaxin_x-shuaxin_SIZE_x/2;
    public static float shuaxin_TOUCH_RIGHT_x=shuaxin_x+shuaxin_SIZE_x/2;
    public static float shuaxin_TOUCH_BOTTOM_y=shuaxin_y+shuaxin_SIZE_y/2;
    public static float shuaxin_TOUCH_TOP_y=shuaxin_y-shuaxin_SIZE_y/2;
    
    public static int MoneyBoxId;
    public static int MoneyId;
    public static float Money_x=50;
    public static float Money_y=100;
    public static float Money_SIZE_x=150;
    public static float Money_SIZE_y=150;
    



    
    
    
    public static float holeboxx=1.4f;
    public static float holeboxy=0.5f;
    public static float holeboxz=14.8f;

    public static float boxbian1x=0.985f;
    public static float boxbian1y=0.428f;
    public static float boxbian1z=0.010f;

    public static float boxbian2x=0.010f;
    public static float boxbian2y=0.428f;
    public static float boxbian2z=0.985f;
    
    public static float dollinitx=-0.85f;
    public static float dollinity=1f;
    public static float dollinitz=11.4f;
    public static float spanx=0.6f;
    public static float spanz=1.0f;
    public static boolean isupdate=false;
    

    public static float pig1x=1f;
    public static float pig1y=1f;
    public static float pig1z=13f;

    public static float pig0x=-1f;
    public static float pig0y=1f;
    public static float pig0z=12f;

    public static float pigbl=0.8f;

    public static float bodyadd1x=0.5f*pigbl;
    public static float bodyadd1y=0.25f*pigbl;
    public static float bodyadd1z=0.13f*pigbl;
    
    public static float bodyadd2x=0.5f*pigbl;
    public static float bodyadd2y=0.12f*pigbl;
    public static float bodyadd2z=0.40f*pigbl;


    public static float bodyc= 0.68f*pigbl;
    public static float bodyg= 0.40f*pigbl;
    public static float bodyk= 0.50f*pigbl;

    public static float footc= 0.134f*pigbl;
    public static float footg= 0.059f*pigbl;
    public static float footk= 0.116f*pigbl;


    public static float nosec= 0.110f*pigbl;
    public static float nosek= 0.329f*pigbl;
    public static float noseg= 0.260f*pigbl;
    

    public static float niux=1.5f;
    public static float niuy=1f;
    public static float niuz=11f;

    public static float niubz=0.8f;

    public static float niubodyx=0.80f*niubz;
    public static float niubodyy=0.45f*niubz;
    public static float niubodyz=0.48f*niubz;

    public static float niufootx=0.1f*niubz;
    public static float niufooty=0.1f*niubz;
    public static float niufootz=0.1f*niubz;

    public static float niuadd1x=0.65f*niubz;
    public static float niuadd1y=0.11f*niubz;
    public static float niuadd1z=0.35f*niubz;

    public static float niuadd2x=0.55f*niubz;
    public static float niuadd2y=0.35f*niubz;
    public static float niuadd2z=0.08f*niubz;

    public static float niuadd3x=0.10f*niubz;
    public static float niuadd3y=0.20f*niubz;
    public static float niuadd3z=0.30f*niubz;


    
    public static float phonex=0.5f;
    public static float phoney=1;
    public static float phonez=13;

    public static float phonebz=0.8f;

    public static float phoneyr=0.13f*phonebz;
    public static float phoneyh=0.412f*phonebz;


    public static float phone1c=0.394f*phonebz;
    public static float phone1k=0.48f*phonebz;
    public static float phone1g=0.261f*phonebz;



    public static float phone2c=0.394f*phonebz;
    public static float phone2k=0.394f*phonebz;
    public static float phone2g=0.226f*phonebz;

    public static float phone3c=0.394f*phonebz;
    public static float phone3k=0.48f*phonebz;
    public static float phone3g=0.102f*phonebz;
    
    public static float tvx=2;
    public static float tvy=1;
    public static float tvz=11;

    public static float tvbz=0.9f;

    public static float tvbodyx=0.580f*tvbz;
    public static float tvbodyy=0.516f*tvbz;
    public static float tvbodyz=0.552f*tvbz;

    public static float tvfootx=0.106f*tvbz;
    public static float tvfooty=0.045f*tvbz;
    public static float tvfootz=0.09f*tvbz;


    public static float tvtopr=0.149f*tvbz;

    public static float tvtopmr=0.051f*tvbz;

    public static float tvangle=45*tvbz;

    public static float tvyzr=0.035f*tvbz;
    public static float tvyzh=0.111f*tvbz;

    

    public static float catch_left=800;
    public static float catch_right=1020;
    public static float catch_top=1590;
    public static float catch_bottom=1810;


    public static float tol_left=0;
    public static float tol_right=165;
    public static float tol_top=1630;
    public static float tol_bottom=1800;


    public static float tor_left=235;
    public static float tor_right=405;
    public static float tor_top=1630;
    public static float tor_bottom=1800;


    public static float tou_left=100;
    public static float tou_right=300;
    public static float tou_top=1500;
    public static float tou_bottom=1710;


    public static float tod_left=100;
    public static float tod_right=300;
    public static float tod_top=1750;
    public static float tod_bottom=1910;




    public static final float cubeSize=0.4f;

    public static final float Ball_R=1f;
    public static final float Ball_Height=0f;

    public static final float Stick_Length=5f;
    public static final float Stick_R=0.2f;
    public static final float Stick_Height=4f;

    public static final float Ceiling_Height=8f;
    public static final float Floor_Height=-2f;

    public static final float LEG_MASS=1f;



    public  static boolean keyFlag=true;




    
    public static boolean isCollection=false;
    public static boolean isleft=false;
    public static boolean isright=false;
    public static boolean istop=false;
    public static boolean isbottom=false;

    public static int[] dollcount=new int[9];
    public static int moneycount;

    public static float menu_left=890;
    public static float menu_right=1100;
    public static float menu_top=50;
    public static float menu_bottom=160;

    public static float backyouxi_left=290;
    public static float backyouxi_right=790;
    public static float backyouxi_top=485;
    public static float backyouxi_bottom=610;

    public static float yinxiao_left=290;
    public static float yinxiao_right=790;
    public static float yinxiao_top=675;
    public static float yinxiao_bottom=800;

    public static float yinyue_left=290;
    public static float yinyue_right=790;
    public static float yinyue_top=915;
    public static float yinyue_bottom=1024;


    public static float collection_left=290;
    public static float collection_right=790;
    public static float collection_top=1074;
    public static float collection_bottom=1232;

    public static float backmenu_left=290;
    public static float backmenu_right=790;
    public static float backmenu_top=1320;
    public static float backmenu_bottom=1450;

    public static boolean yinxiaoIsOn=true;
    public static boolean yinyueIsOn=true;

    

    public static float back_left=32;
    public static float back_right=232;
    public static float back_top=1650;
    public static float back_bottom=1850;

    public static float initdatax=160;
    public static float initdatay=650;
    

    public static float sell_left=490;
    public static float sell_right=720;
    public static float sell_top=1400;
    public static float sell_bottom=1620;

    
    public static float scoreback_left=32;
    public static float scoreback_right=232;
    public static float scoreback_top=1550;
    public static float scoreback_bottom=1750;

    public static int allcount=100;
    public static int getcount=60;
    public static int failcount=0;
    public static int getcollectionpercent=0;
    public static int getdolltypecount=0;

}
