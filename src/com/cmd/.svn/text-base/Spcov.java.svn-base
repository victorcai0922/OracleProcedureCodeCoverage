/**
 * 
 */
package com.cmd;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;

import com.plsql.Coverage;

/**
 * @author caiql
 */
public class Spcov {
	
	private Logger logger = Logger.getLogger(Spcov.class);
	private static Options options = new Options();
	private Coverage cov = new Coverage();
	
	static{
		options.addOption("create",false,"create the coverage statics table!");
		options.addOption("delete",false,"delete Statics Table!");
		options.addOption("report",false,"generate the coverage report!");
		options.addOption("version",false,"get the version of coverage tool!");
	}
	
	/**
	 */
	public void startCmd(String filePath,String funcId){
		System.out.println("----start collect the code coverage----");
		cov.start(filePath,funcId);
	}
	
	public void creatStaticsCmd(){
		
	}
	
	public void creatStaticsTableCmd(){
		cov.initialize();
		cov.createProfilertables();
	}
	
	public void deleteStaticsTableCmd(){
		System.out.println("----delete the statics table start----");
		cov.initialize();
		cov.cleanUp();
		System.out.println("----delete the statics table finish----");
	}
	
	/**
	 */
	public void stopCmd(){
		System.out.println("----stop collect the coverage----");
		cov.stop();
	}
	
	/**
	 */
	public void reportGenerateCmd(){
		System.out.println("----generate code coverage report----");
		cov.initialize();
		System.out.println("--------------------------------------");
		cov.report();
		System.out.println("--------------------------------------");
		stopCmd();
	}
	
	public void allCommand(String[] args){
		HelpFormatter formatter = new HelpFormatter();
		String formatstr = "spcov [-start][-end][-version][-create][-delete][-report]";
		CommandLineParser parser = new PosixParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			formatter.printHelp(formatstr, options);
		}
		if(cmd.hasOption("report")){
			reportGenerateCmd();
		}
		if(cmd.hasOption("delete")){
			deleteStaticsTableCmd();
		}
		if(cmd.hasOption("version")){
			System.out.println("cffex plsql code coverage ver1.0");
		}
		
		if(cmd.hasOption("create")){
			System.out.println("----start create statics table!----");
			creatStaticsTableCmd();
			System.out.println("----end create statics table!----");
		}
	}
	
	public static void main(String[] args) {
		Spcov spcov = new Spcov();
		args = new String[2];
		args[0] = new String("java -jar spcov.jar");
		args[1] = new String("-report");
//		System.out.println(args);
		spcov.allCommand(args);
	}

}
