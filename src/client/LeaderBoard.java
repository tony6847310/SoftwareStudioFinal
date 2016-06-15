package client;

import java.io.*;
import static java.lang.System.*;

public class LeaderBoard {
	
	/*public static void main(String[] args){
		new LeaderBoard();
		new LeaderBoard().Update("playerqq", 110);
	}*/
	
	public LeaderBoard(Gamestage gs){
		
		FileReader fr;
		BufferedReader br;
		String str;
		
		try{
			fr = new FileReader("res/leaderboard.txt");
			br = new BufferedReader(fr);
			/*while((str = br.readLine()) != null){
				out.println(str);
			}*/
			br.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void Update(String player, int score){
		String oldFileName = "res/leaderboard.txt";
		String tmpFileName = "res/tmp_leaderboard.txt";
		BufferedReader br = null;
		BufferedWriter bw = null;
		String str, pre_str;
		int linecounter;
		int insert_state; // 0 -> before, 1 -> now, 2 -> after
		
		try{
			br = new BufferedReader(new FileReader(oldFileName));
			bw = new BufferedWriter(new FileWriter(tmpFileName));
			linecounter = 0;
			insert_state = 0;
			pre_str = null;
			
			while((str = br.readLine())!= null){
				linecounter++;
				if(linecounter%2 == 0){
					String[] AfterSplit_str = str.split(" ");
					//out.println(AfterSplit_str[1]);
					int tmpscore = Integer.valueOf(AfterSplit_str[1]);
					if((score >= tmpscore) && (player != null)){
						String str_score = Integer.toString(score);
						String insert = player + "---------- " + str_score;
						bw.write(insert + "\n");
						player = null;
						score = -1;
						insert_state = 1;
					}
				}
				if(insert_state == 0)
					bw.write(str + "\n");
				else if(insert_state == 1){
					insert_state = 2;
					pre_str = str;
				}
				else if(insert_state == 2){
					if(linecounter%2 == 0){
						bw.write(pre_str + "\n");
						pre_str = str;
					}
					else
						bw.write(str + "\n");
				}
			}
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			try{
				if(br != null)
					br.close();
			} catch (Exception e){
				e.printStackTrace();
			}
			try{
				if(bw != null)
					bw.close();
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		
		// Once everything is complete, delete old file..
	    File oldFile = new File(oldFileName);
	    oldFile.delete();

	    // And rename tmp file's name to old file name
	    File newFile = new File(tmpFileName);
	    newFile.renameTo(oldFile);
	}
}
