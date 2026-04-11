    import javax.sound.sampled.*;
    import java.io.IOException;
    import java.net.URL;
    import java.util.Scanner;

    public class Gobang {

        //设置静态变量用于棋盘制作时对间隔控制
        //静态方法中只能访问静态变量
        public static String row_gap="────";
        public static String column_gap="│    │    │    │    │    │    │    │    │    │";

        //1.b对棋盘进行存储
        public static char[][] cheesboard={{'┌','┬','┬','┬','┬','┬','┬','┬','┬','┐'},
                {'├','┼','┼','┼','┼','┼','┼','┼','┼','┤'},
                {'├','┼','┼','┼','┼','┼','┼','┼','┼','┤'},
                {'├','┼','┼','┼','┼','┼','┼','┼','┼','┤'},
                {'├','┼','┼','┼','┼','┼','┼','┼','┼','┤'},
                {'├','┼','┼','┼','┼','┼','┼','┼','┼','┤'},
                {'├','┼','┼','┼','┼','┼','┼','┼','┼','┤'},
                {'├','┼','┼','┼','┼','┼','┼','┼','┼','┤'},
                {'├','┼','┼','┼','┼','┼','┼','┼','┼','┤'},
                {'└','┴','┴','┴','┴','┴','┴','┴','┴','┘'}};


        //定义一个变量用于控制玩家一共落子的次数 奇数为玩家A 偶数为玩家B
        public static int times=0;

        //玩家的棋子
        public static char pieceA='○';
        public static char pieceB='■';

        //用于玩家落子时根据行列标输入
        public static int row=0;
        public static int column=0;

        //主函数
        public static void main(String[] args){
            Show_Cheesboard();
            Scanner sc=new Scanner(System.in);
            //棋盘容量
            int totalPosition=cheesboard.length*cheesboard[0].length;
            //当玩家总落子数达到棋盘容量上限时 游戏结束
            while(times<totalPosition){
                //堆玩家落子判断并给出提示
                System.out.println((times%2==0?"玩家B落子":"玩家A落子"));
                //提示玩家输入落子位置
                System.out.println("请输入落子位置((row,column)：0<=row<="+(cheesboard[0].length-1)+"0<=column<="+(cheesboard.length-1) +"):");

                //落子
                while(true){
                    //避免玩家不看输入提示 当第一个输入为整数 就认为玩家看到了这个提示
                    if(sc.hasNextInt()){
                        row=sc.nextInt();
                        column=sc.nextInt();
                        //对落子位置进行检验 首先这个位置不能超出棋盘区域
                        if(row>=0&&row<cheesboard[0].length&&column>=0&&column<cheesboard.length){
                            //对落子位置进行检查  接着这个位置不能已经存在之前下过的棋子
                            if(cheesboard[row][column]==pieceA||cheesboard[row][column]==pieceB){
                                //只要这个位置放了某一个棋子 就无法落子
                                System.out.println("非法落子，请重新选择落子位置");
                                playAudio("illegal.wav");
                            }else{
                                //这个位置没有放任何棋子 可以正常落子
                                cheesboard[row][column]=(times%2==0)?pieceB:pieceA;
                                playAudio("fill.wav");
                                //正常落子之后退出即可
                                break;
                            }
                        }else{
                            //如果落子超出对玩家进行提示然后重新输入
                            System.out.println("非法落子，请重新选择落子位置");
                            playAudio("illegal.wav");
                        }
                    }else{
                        System.out.println("非法落子，请重新选择落子位置");
                        playAudio("illegal.wav");
                        sc.next();//这里需要把玩家非法输入的非数字从Scanner中取出
                    }
                }


                //合法落子之后  重新展示棋盘
                Show_Cheesboard();

                //判断是否有玩家获胜
                if(Win()){
                    //有玩家获胜
                    System.out.println((times%2==0)?"玩家B获得胜利":"玩家A获得胜利");
                    playAudio("win.wav");
                    break;
                }
                times++;
            }
            //判断棋盘是否已满
            if(times==totalPosition){
                //棋盘已满还未分出胜负说明平局
                System.out.println("平局");
            }

        }

        //判断是否有玩家获胜
        public static boolean Win(){
            //定义四个boolean类型变量存储每种情况是否满足
            boolean case1=true;
            boolean case2=true;
            boolean case3=true;
            boolean case4=true;
            char curr=(times%2==0)?pieceB:pieceA;
            for(int i=0;i<cheesboard.length;i++){
                for(int j=0;j<cheesboard[0].length;j++){
                    //case 1:五颗棋子在同一条水平直线上 (i,j) (i,j+1) (i,j+2) (i,j+3) (i,j+4)
                    case1=(j+4<cheesboard[0].length)&&cheesboard[i][j]==curr&&cheesboard[i][j+1]==curr&&
                            cheesboard[i][j+2]==curr&&cheesboard[i][j+3]==curr&&cheesboard[i][j+4]==curr?true:false;

                    //case 2:五颗棋子在同一条竖直直线上 (i,j) (i+1,j) (i+2,j) (i+3,j) (i+4,j)
                    case2=(i+4<cheesboard.length)&&cheesboard[i][j]==curr&&cheesboard[i+1][j]==curr&&
                            cheesboard[i+2][j]==curr&&cheesboard[i+3][j]==curr&&cheesboard[i+4][j]==curr?true:false;

                    //case 3:五颗棋子自左下至右上呈45° (i,j) (i+1,j-1) (i+2,j-2) (i+3,j-3) (i+4,j-4)
                    case3=(i+4<cheesboard.length)&&(j>=4)&&cheesboard[i][j]==curr&&cheesboard[i+1][j-1]==curr&&
                            cheesboard[i+2][j-2]==curr&&cheesboard[i+3][j-3]==curr&&cheesboard[i+4][j-4]==curr?true:false;

                    //case 4:五颗棋子自左下至右上呈135° (i,j) (i+1,j+1) (i+2,j+2) (i+3,j+3) (i+4,j+4)
                    case4=(i+4<cheesboard.length)&&(j>=4)&&cheesboard[i][j]==curr&&cheesboard[i+1][j+1]==curr&&
                            cheesboard[i+2][j+2]==curr&&cheesboard[i+3][j+3]==curr&&cheesboard[i+4][j+4]==curr?true:false;
                }
            }
            return case1&&case2&&case3&&case4;
        }

        //输出棋盘
        public static void Show_Cheesboard(){
            //打印棋盘的列标
            System.out.print(" ");
            for(int t=0;t<cheesboard[0].length;t++){
                System.out.print(t);
                Show_RowGap();
            }
            System.out.println();
            for(int i=0;i<cheesboard.length;i++){
                //打印棋盘的行标
                System.out.print(i);
                for(int j=0;j<cheesboard[i].length;j++){
                    //如果是最后一列就不必在后面打印列分隔符
                    if(j!=cheesboard[i].length-1)
                        System.out.print(cheesboard[i][j]+row_gap);
                    else{
                        System.out.print(cheesboard[i][j]);
                    }
                }
                System.out.println();
                //如果是最后一行就不需要在后面打印行分隔符
                if(i!=cheesboard.length-1){
                    System.out.print(" ");
                    System.out.println(column_gap);
                }
            }
        }

        //输出跟行间隔等长的空字符
        public static void Show_RowGap(){
            for(int i=0;i<row_gap.length();i++){
                System.out.print(" ");
            }
        }


        //播放声音
        public static void playAudio(String fileName) {
            try {
                // 1. 获取音频文件资源
                URL url = Gobang.class.getResource(fileName);
                if (url == null) {
                    System.err.println("音频文件未找到: " + fileName);
                    return;
                }

                // 2. 创建音频输入流
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);

                // 3. 获取音频播放器 Clip
                Clip clip = AudioSystem.getClip();

                // 4. 打开音频流（数据加载）
                clip.open(audioStream);

                // 5. 开始播放
                clip.start();

                // 6. 可选：让程序等待音频播放完毕（如果不加这行，音频可能会被突然终止）
                // clip.drain(); // 等待音频播放完当前缓冲区
                // 或者使用 Thread.sleep 等待播放时长，但简单起见，这里让 clip 自己播放

            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }

    }
