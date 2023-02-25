import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * @author zhangwei
 * @date 2020/6/17 19:02
 */
public class GoLibrary {

    /***
     * @Title：
     * @Description: 主函数
     * @Param: [args]
     * @return: void
     * @Author: 张伟
     * @Date: 2020/6/17
     */

    public static void main(String[] args) throws ParseException {

      Run();
        //getSeat();
    }


    private static Logger logger = Logger.getLogger(GoLibrary.class.getName());

    public static Robot robot; //自动化类
    public static Properties properties;    //配置文件解析类
    public static InputStream inputStream;  //字符流类
    public static Boolean flag = true;

    public static int x1 = 0;
    public static int x2 = 0;
    public static int x3 = 0;

    public static int x4 = 1469;
    public static int x5 = 1371;

    public static int y1 = 0;
    public static int y2 = 0;
    public static int y3 = 0;



    public static  Pointer yyPointer ;
    public static  Pointer zw1Pointer;
    public static  Pointer zw2Pointer;



//    public static Pointer pointer1 =new Pointer(1469,521);
//    public static Pointer pointer2 =new Pointer(1371,1024);




    /***
     * @Title：
     * @Description: 初始化参数
     * @Param:
     * @return:
     * @Author: 张伟
     * @Date: 2020/6/19
     */
    static {
        try {
            robot = new Robot();
            properties = new Properties();
            inputStream = Object.class.getResourceAsStream("/pointer.properties");
            properties.load(inputStream);
            x1 = getPropertiesToInt("yyPointer_x");
            x2 = x1 + 200;
            x3 = x1 + 580;

            y1 = getPropertiesToInt("yyPointer_y");
            y2 = y1+100;
            y3 = y2;

            yyPointer  = new Pointer(x1, y1);  //明日预约坐标
            zw1Pointer = new Pointer(x2, y2); //座位1坐标
            zw2Pointer = new Pointer(x3, y3); //座位2坐标
        } catch (Exception e) {
            e.printStackTrace();
        }
    }











    /***
     * @Title：
     * @Description: 获取指定坐标点的颜色RGB值
     * @Param: [x, y]
     * @return: java.awt.Color
     * @Author: 张伟
     * @Date: 2020/6/17
     */
    public static Color getPointerColor(Pointer pointer) {
        return robot.getPixelColor(pointer.x, pointer.y);
    }





    /***
     * @Title：
     * @Description: 点击
     * @Param:
     * @return: void
     * @Author: 张伟
     * @Date: 2020/6/17
     */
    public static void click(Pointer pointer) {
        //鼠标移动到 x,y
        robot.mouseMove(pointer.x, pointer.y);
        //按下鼠标
        robot.mousePress(MouseEvent.BUTTON1_MASK);
        //释放鼠标
        robot.mouseRelease(MouseEvent.BUTTON1_MASK);

//        Timestamp timestamp=new Timestamp(System.currentTimeMillis());//转换成系统时间时分秒,不加的话是一串时间戳
//        logger.info("点击-----"+timestamp);
    }

    /***
     * @Title：
     * @Description: 点击2 ，当坐标12不确定时使用
     * @Param:
     * @return: void
     * @Author: 张伟
     * @Date: 2020/6/17
     */
    public static void click2(int x ,int y) {
        //鼠标移动到 x,y
        robot.mouseMove(x, y);
        //按下鼠标
        robot.mousePress(MouseEvent.BUTTON1_MASK);
        //释放鼠标
        robot.mouseRelease(MouseEvent.BUTTON1_MASK);
    }


    /***
     * @Title：
     * @Description: 将配置文件信息转为int数据
     * @Param: [pointerStr]
     * @return: int
     * @Author: 张伟
     * @Date: 2020/6/19
     */
    public static int getPropertiesToInt(String pointerStr) {
        return Integer.parseInt((String) properties.get(pointerStr));
    }


    /***
     * @Title：用户主动退出，防止死循环
     * @Description:
     * @Param: []
     * @return: void
     * @Author: 张伟
     * @Date: 2020/6/19
     */
    public static void Break() {
        Point p = MouseInfo.getPointerInfo().getLocation();
        int x = (int) p.getX();
        if (x != x1 && x != x2 && x != x3 && x != x4 && x != x5) {
            Timestamp timestamp=new Timestamp(System.currentTimeMillis());//转换成系统时间时分秒,不加的话是一串时间戳
            logger.info("用户主动退出"+timestamp);
            flag = false;
        }
    }




    /***
     * @Title：
     * @Description: 定时抢座
     * @Param: []
     * @return: void
     * @Author: 张伟
     * @Date: 2020/6/19
     */
    public static void Run() throws ParseException {

        String startTimeStr = (String) properties.get("startTimeStr");//"22:00:00:000" 开始时间

        // 一天的毫秒数
        long daySpan = 24 * 60 * 60 * 1000;

        // 规定的每天时间22:00:00运行
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd " + startTimeStr);
        // 首次运行时间
        Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").parse(sdf.format(new Date()));

        // 如果今天的已经过了 首次运行时间就改为明天
        if (System.currentTimeMillis() > startTime.getTime())
            startTime = new Date(startTime.getTime() + daySpan);

        Timer t = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                getSeat();
            }
        };
        // 以每24小时执行一次
        t.scheduleAtFixedRate(task, startTime, daySpan);
    }



    /***
    * @Title：
    * @Description: 自定义日志打印函数
    * @Param: [str]
    * @return: void
    * @Author: 张伟
    * @Date: 2020/6/20
    */
    public static void myLogger(String str){
        Timestamp timestamp=new Timestamp(System.currentTimeMillis());//转换成系统时间时分秒,不加的话是一串时间戳
        System.out.println(timestamp+"........"+str);
    }
    /***
    * @Title：
    * @Description: 抢座位开始
    * @Param: []
    * @return: void
    * @Author: 张伟
    * @Date: 2020/6/20
    */
    public static void getSeat() {
        myLogger("开始抢课");
        click(yyPointer);//点击明日预约
        Color color;
        while (flag) {
            Break();
            color = getPointerColor(yyPointer);
            if (color.getRed() == 141 && color.getGreen() == 141 && color.getBlue() == 141) {
                myLogger("点击未响应");
            } else if (color.getRed() == 255 && color.getGreen() == 255 && color.getBlue() == 255) {
                myLogger("页面加载中");
            } else if(color.getRed() == 187 && color.getGreen() == 155 && color.getBlue() == 141){
                myLogger("页面渲染中");
            } else{
                //System.out.println(color.toString());
                color = getPointerColor(zw1Pointer);
                System.out.println(color.toString());
                if((color.getRed()==92 && color.getGreen()==132 && color.getBlue()==189) || (color.getRed()==172 && color.getGreen()==172 && color.getBlue()==172)){
                    while (flag) {
                        Break();
                        click(zw1Pointer);//点击座位1
                        myLogger("狂点座位1");
                        color = getPointerColor(zw1Pointer);
                        System.out.println(color.toString());
                        if (color.getRed() == 172 && color.getGreen() == 172 && color.getBlue() == 172) {//座位1未抢到,准备抢座位2
                            while (flag) {
                                Break();
                                click(zw2Pointer);//点击座位2
                                myLogger("狂点座位2");
                                color = getPointerColor(zw2Pointer);
                                if (color.getRed() == 172 && color.getGreen() == 172 && color.getBlue() == 172) {//座位2也没抢到
                                    myLogger("抢座失败");
                                    break;
                                } else if (color.getRed() == 222 && color.getGreen() == 230 && color.getBlue() == 242) {
                                    myLogger("抢座成功");
                                    break;
                                }
                            }
                            break;
                        } else if (color.getRed() == 222 && color.getGreen() == 230 && color.getBlue() == 242) {
                            myLogger("抢座成功");
                            break;
                        }
                    }
                    break;
                }
            }
        }
        myLogger("抢座结束");
    }
}
