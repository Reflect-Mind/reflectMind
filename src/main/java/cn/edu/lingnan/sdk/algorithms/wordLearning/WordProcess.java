package cn.edu.lingnan.sdk.algorithms.wordLearning;

import cn.edu.lingnan.pojo.Vocab;

import java.util.List;

public class WordProcess {

	//发现新单词，并将新单词临时保存到vlist中
	public int newWord( String str1, List<Vocab> clist, List<Vocab> vlist ) {

		int i, j, flag, wordlen;
		String str2;
		CharCalc calc = new CharCalcImpl();

		//1.如果WordLearning.freqConst等于-1，表示词频阈值将由系统确定，否则是人工确定
		if( WordLearning.freqConst == -1 ) {

			//通过文章总数来确定最小出现次数minAppear（需要四舍五入，minAppear最小为4）---------词频阈值
			WordLearning.freqConst = (int) Math.round( ( (double) str1.length() / 2500 ) );
			if( WordLearning.freqConst < 4 )
				WordLearning.freqConst = 3;
		}

//		System.out.println("最低单词频数: " + WordLearning.freqConst );
		long time1 = System.currentTimeMillis();	//计时


		//2.循环查找单词（最长单词为WordLearning.wordLength字符）-------------------词长阈值
		//开始学习新单词
		for( i=0; i<str1.length()-2-1; i++ ) {

			//截取2个字符作为str2
			str2 = str1.substring( i, i+2 );

			//如果单词不存在于词汇表中，则会被检查
			if( str2.indexOf("_") < 0 )	//单词str2中不应存在"_"
				if( !calc.wordExist(str2, vlist ) ) {
					calc.wordAnalyze(str1, str2, clist, vlist, WordLearning.freqConst );
				}
		}
		long time2 = System.currentTimeMillis();	//计时

		//学习新单词（此时是长度大于2的单词）
		for( wordlen=3; wordlen <= WordLearning.wordLength; wordlen++ ) {

			for( i=0, flag=0; i < str1.length()-wordlen-1; i++ ) {

				//截取wordlen个字符作为str2片段
				str2 = str1.substring( i, i+wordlen );

				flag=1;	//说明str2片段可以继续筛选
				if( flag==1 && !calc.wordExist(str2, vlist ) ) {
					calc.wordAnalyze(str1, str2, clist, vlist, WordLearning.freqConst );
				}
			}

		}
		long time3 = System.currentTimeMillis();	//计时

		long time21 = time2 - time1;					//计时
		long time32 = time3 - time2;					//计时

//		System.out.println("学习双字符词用时：" + time21 + "ms");
//		System.out.println("学习多字符词用时：" + time32 + "ms");

		return vlist.size();
	}
}
