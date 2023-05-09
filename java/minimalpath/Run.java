package minimalpath;


import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

class Run {

    public static void aStarTest(){
        int[][] puzzle={{2,4,3},{6,1,5},{7,8,0}};
        AStar aStar=new AStar();
        String result=aStar.aStar(puzzle);
        System.out.println(result);
    }

    public static void main(String[] args) {
        double start=System.currentTimeMillis();
        OperatingSystemMXBean osb= ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        aStarTest();
        double end=System.currentTimeMillis();
        Runtime.getRuntime().gc();
        long usedMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        System.out.print((usedMemory/1024) + " kb");
        System.out.println("\n 실행시간 : "+ (end-start) );

    }

}