package cn.edu.lingnan.sdk.algorithms.wordLearning;

import cn.edu.lingnan.pojo.Vocab;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.io.IOException;

public class CharCalcImpl implements CharCalc {

    //读取文件到str1
    @Override
    public String readTextFile( File file ) throws IOException {

        Reader reader = null;
        int tempchar;
        char tc;
        int i=0, num, j, k, flag;

        //用List存储字符
        List<Character> charList = new ArrayList<>();
        StringBuilder bu = new StringBuilder();
        String str1;

        //将reader中的不同字符存储到字符列表charList中
        reader = new InputStreamReader( (new FileInputStream(file)), "UTF-8" );
        while( ( tempchar = reader.read()) != -1 ) {
            tc = (char) tempchar;

            // 只选择汉字字符（已失效）, 2018/1/31
//            if( tempchar>=19968 && tempchar<=40869 )
            charList.add( tc );
        }
        reader.close();

        //将字符列表charList转化为字符串
        for (Character character : charList) {
            bu.append( character );
        }
        str1 = bu.toString();

        return str1;
    }


    //对原文text进行预处理，以去掉非汉字字符
    @Override
    public String preProcessText(String text) {

        char tc;
        int i;
        String str1;

        //用List存储字符
        List<Character> charList = new ArrayList<>();
        StringBuilder bu = new StringBuilder();

        for( i=0; i<text.length(); i++ ) {

            tc = text.charAt(i);

            //只选择汉字字符
            if( tc>=19968 && tc<=40869 ) {
                charList.add( tc );
            }
        }

        //将字符列表charList转化为字符串
        for (Character character : charList) {
            bu.append( character );
        }
        str1 = bu.toString();

        return str1;
    }


    //将文本text中的不同字符存储到clist列表并统计频数
    @Override
    public List<Vocab> readChar( String text, List<Vocab> clist ) throws IOException {

        int i, j, k, flag, temp;
        char tc;
        String ts;

        //将文本text中的不同字符存储到clist列表并统计频数
        for( i=0; i<text.length(); i++ ) {

            tc = text.charAt(i);
            ts = String.valueOf(tc);

            // 只选择汉字字符, 已失效, 2018/2/1
//            if( tc>=19968 && tc<=40869 ) {

                //检查clist中是否存在字符tc
                for( j=0, flag=0; j<clist.size(); j++ )
                    if( clist.get(j).getContent().equals( ts ) ) {

                        //如果字符tc存在于clist中，更新clist中对应字符的频数
                        temp = clist.get(j).getAppearnum();
                        clist.get(j).setAppearnum( temp + 1 );

                        //flag=1表示字符tc存在于clist中
                        flag = 1;
                    }
                if( flag != 1 ) {
                    //如果字符tc不存在于clist中，在clist中增加tc
                    Vocab vc = new Vocab();
                    vc.setContent( ts );
                    vc.setAppearnum( 1 );
                    clist.add( vc );
                }
//            }
        }

        return clist;
    }


    //对词汇列表按某种规则排序
    @Override
    public List<Vocab> sortVocab(List<Vocab> clist) {

        //按频数对clist排序
        Collections.sort( clist, new Comparator<Vocab>() {
            public int compare( Vocab arg0, Vocab arg1 ) {
                return arg1.getAppearnum().compareTo( arg0.getAppearnum() );
            }
        }
        );

        return clist;
    }

    @Override
    public boolean wordExist(String str2, List<Vocab> vlist) {

        boolean flag=false;
        int i;

        //此处可以通过排序加快检索过程
        for( i=0; i<vlist.size(); i++ ) {
            if( vlist.get(i).getContent().equals( str2 ) )
                flag=true;
        }
        return flag;
    }

    //分析str2是否构成一个单词
    @Override
    public boolean wordAnalyze(String str1, String str2, List<Vocab> clist, List<Vocab> vlist, int minAppear) {

        //当flag为true时，说明str2构成一个单词
        boolean flag=false;

        int appear;			//单词的出现次数
        int appear1;		//字符1的出现次数
        int appear2;		//字符2的出现次数
        int ind;			//字符串匹配时用到的位置下标
        int j;				//循环用到的递增变量

        double solidDeg = 0;	//单词的凝合程度
        double solidDeg1;	//单词的凝合程度
        double solidDeg2;	//单词的凝合程度

        //计算单词str2在文本str1中的出现次数appear
        for( j=0, appear=0, ind=-1; j < str1.length(); ) {
            ind = str1.indexOf( str2, j );
            if( ind>=0 ) {
                appear++;
                j = ind + str2.length();
            }
            else
                j = str1.length();
        }

        //如果某单词的出现次数大于阈值，则会被继续检查
        if( appear>minAppear )	{

            //计算单词str2的凝合程度
            //凝合程度 = 总数/子词数/子词数*母词数	2018/1/15
            if( str2.length() == 1 ) {
                //str2是单字, 赋予一个凝合程度, 2018/1/26
                solidDeg = WordLearning.solidConst + 1;
            }
            if( str2.length() == 2 ) {
                //str2是双字符词"XY"
                appear1 = findChar( str2, 0, clist );
                appear2 = findChar( str2, 1, clist );
                //得出X+Y的凝合程度
                solidDeg =  (double) str1.length() / ( appear1*appear2 ) * appear;
            }
            if( str2.length() > 2 ) {
                //str2是多字符词"XYZ"
                appear1 = findChar( str2, 0, clist );//find X
                appear2 = findWord( str2, 1, vlist, str2.length()-1 );//find YZ
                //得出X+YZ的凝合程度
                solidDeg1 =  (double) str1.length() / ( appear1*appear2 ) * appear;

                appear1 = findChar( str2, 2, clist );//find Z
                appear2 = findWord( str2, 0, vlist, str2.length()-1 );//find XY
                //得出XY+Z的凝合程度
                solidDeg2 =  (double) str1.length() / ( appear1*appear2 ) * appear;

                //最终凝合程度取较小值
                solidDeg = ( solidDeg1<solidDeg2 )?solidDeg1:solidDeg2;
            }

            if( solidDeg > WordLearning.solidConst ) {
                //如果某单词的凝固程度大于凝固程度阈值, 则会被加入词汇表vlist
                Vocab voc = new Vocab();
                voc.setContent( str2 );
                voc.setAppearnum( appear );
                voc.setSolid( solidDeg );
                vlist.add( voc );
//				System.out.println( str2 + ", 频数: " + appear );
            }
        }

        return flag;
    }


    //确定单词str2中某字符的频数
    @Override
    public int findChar(String str2, int loc, List<Vocab> clist) {

        int i, appear;
        char ch;
        String chs;

        ch = str2.charAt(loc);
        chs = String.valueOf(ch);

        //在clist中找到指定字符ch
        for( i=0, appear=-1; i<clist.size(); i++ )
            if( clist.get(i).getContent().equals( chs ) )
                appear = clist.get(i).getAppearnum();

        return appear;
    }


    //确定单词str2中某单词的出现次数
    @Override
    public int findWord( String str2, int loc, List<Vocab> vlist, int plus ) {

        int i, appear;
        String ste;

        ste = str2.substring( loc, loc+plus );
        //在vlist中找到指定字符串ste
        for( i=0, appear=-1; i<vlist.size(); i++ )
            if( vlist.get(i).getContent().equals( ste ) )
                appear = vlist.get(i).getAppearnum();

        return appear;
    }


    //计算信息熵
    @Override
    public void entropfy( String strX, List<Vocab> vlist ) { //debugging

        int i, j, k, ind;
        double lentro, rentro, entro;
        char lch, rch;
        List<Vocab> len = new ArrayList<>();	//左邻字频数统计的对象
        List<Vocab> ren = new ArrayList<>();	//左邻字频数统计的对象

        //将词汇表中的所有单词拿去与str匹配
        for( i=0; i<vlist.size(); i++) {
            //entro, len, ren 需要初始化
            entro=0;
            len.clear();
            ren.clear();

            for( j=0; j<strX.length(); j++) {
                ind = strX.indexOf( vlist.get(i).getContent(), j );

                if( ind>=0 ) { //匹配成功时

                    if( (ind-1) >= 0 ) {
                        lch = strX.charAt( ind-1 );
                        countChar( lch, len );	//计算左邻字频数
//                        if( vlist.get(i).getContent().equals( "学宫") ) {
//                            System.out.println("L-" + lch );
//                        }
                    }

                    if( ind + vlist.get(i).getContent().length() < strX.length() ) {
                        rch = strX.charAt( ind + vlist.get(i).getContent().length() );
                        countChar( rch, ren );	//计算右邻字频数
                    }

                    //从ind以后的位置继续匹配
                    k = ind + vlist.get(i).getContent().length();
                    j = k;
                }
                else {	//ind<0，找不到单词，不再循环

                    //计算邻字信息熵
                    lentro = countEntropy(len);
                    rentro = countEntropy(ren);

                    if( lentro <= rentro )
                        entro = lentro;
                    else
                        entro = rentro;
                    vlist.get(i).setEntropy( entro );

                    j=strX.length();
                }
            }
        }
    }


    //对不同字符进行频数计算
    @Override
    public int countChar(char ch, List<Vocab> en) {

        int i, flag, ind, temp;
        String chs;

        //这个if语句目前不会起任何作用
        if( ch<19968 || ch>40869 ) {
            //如果不是汉字字符，就把该邻字当作下划线处理
//            Vocab voc = new Vocab();
//            voc.setContent( "_" );
//            voc.setAppearnum( 1 );
//            en.add( voc );
            return 0;
        }

        chs = String.valueOf(ch);

        for( i=0, flag=0, ind = -1; i<en.size(); i++ )
            if( en.get(i).getContent().equals( chs ) ) {
                //flag标志，当目标字符存在于数组中时，会被赋值为1，同时退出循环
                flag=1;
                ind = i;
                i = en.size();
            }

        if( flag==0 ) {
            //目标字符不存在于字符数组中
            Vocab voc = new Vocab();
            voc.setContent( chs );
            voc.setAppearnum( 1 );
            en.add( voc );

        } else {
            //目标字符已经存在于字符数组中
            temp = en.get(ind).getAppearnum();
            en.get(ind).setAppearnum( temp + 1 );
        }

        return 0;
    }


    //计算邻字信息熵
    @Override
    public double countEntropy(List<Vocab> en) {

        int i, sum;
        double plog, entro;

        if( en.size()==0 )
            return 0;

        //计算邻字信息熵
        for( i=0, sum=0, plog=0, entro=0; i<en.size(); i++) {
            plog = plog + en.get(i).getAppearnum() * Math.log( en.get(i).getAppearnum() );
            sum = sum + en.get(i).getAppearnum();
        }

        entro = ( sum * Math.log(sum) - plog ) / sum;

        return entro;
    }


    //通过信息熵过滤单词
    @Override
    public int filterByEntropy( List<Vocab> vlist) {

        for( int i=0; i<vlist.size(); i++ )
            if( vlist.get(i).getEntropy() < WordLearning.entroConst ) {
                //如果某单词的信息熵小于信息熵阈值，则它会从词汇表中剔除
                vlist.remove(i);
                //当对list进行remove操作后, 要对i进行自减
                i--;
        }

        return vlist.size();
    }


    //用原文检查单词
    @Override
    public int textCheck(String text, List<Vocab> vlist) {

        for( int i=0; i<vlist.size(); i++ ) {
            //如果在原文中找不到相应单词, 则该单词会被删除
            if ( text.indexOf( vlist.get(i).getContent() ) == -1 ) {
//                System.out.println( "原文中不存在: " + vlist.get(i).getContent() );
                vlist.remove(i);
                i--;
            }
        }

        return vlist.size();
    }


    //对vlist的其它列进行初始化
    @Override
    public int initColumn( String text, List<Vocab> vlist ) {

        int i;
        int wordlen;
        double frq;

        //初始化status, wordlen, frq字段
        for( i=0; i<vlist.size(); i++ ) {
            //status
            vlist.get(i).setStatus(1);

            //wordlen
            wordlen = vlist.get(i).getContent().length();
            vlist.get(i).setWordlen( wordlen );

            //frq
            frq = (double) vlist.get(i).getAppearnum() * 1000 / text.length();
            vlist.get(i).setFrq( frq );
        }

        return vlist.size();
    }


}
