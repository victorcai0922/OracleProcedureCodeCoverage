/**
 * 
 */
package com.report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.plsql.PlsqlOperation;

/**
 * @author caiql
 * 
 */
public class CodeCoverageReportGenerator extends Velocity {

	private int totalLine = 0;

	private int totalAnalyzedLine = 0;

	private int totalPackage = 0;

	private String totalCoverage = null;

	private String totalUncoverage = null;

	public int getTotalLine() {
		return totalLine;
	}

	public void setTotalLine(int totalLine) {
		this.totalLine = totalLine;
	}

	public int getTotalAnalyzedLine() {
		return totalAnalyzedLine;
	}

	public void setTotalAnalyzedLine(int totalAnalyzedLine) {
		this.totalAnalyzedLine = totalAnalyzedLine;
	}

	public int getTotalPackage() {
		return totalPackage;
	}

	public void setTotalPackage(int totalPackage) {
		this.totalPackage = totalPackage;
	}

	public String getTotalCoverage() {
		return totalCoverage;
	}

	public void setTotalCoverage(String totalCoverage) {
		this.totalCoverage = totalCoverage;
	}

	public String getTotalUncoverage() {
		return totalUncoverage;
	}

	public void setTotalUncoverage(String totalUncoverage) {
		this.totalUncoverage = totalUncoverage;
	}

	/***
	 * 
	 * @param schema
	 * @param pk_line
	 * @param pk_coverage
	 * @param code_coverage_table
	 * @param source_code
	 * @param line_time
	 * @throws Exception
	 */
	@Deprecated
	public void report(PlsqlOperation po, String schema,
			Map<String, String> pk_line, Map<String, String> pk_coverage,
			Map<String, String> pk_uncoverage,
			Map<String, String> pk_analyzed_line) throws Exception {
		Properties p = new Properties();
		p.setProperty("resource.loader", "class");
		p.setProperty("class.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		p.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
		p.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
		p.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
		Velocity.init(p);
		Template template = Velocity.getTemplate("details.vm");
		Template paktemplate = Velocity.getTemplate("package.vm");
		VelocityContext context = new VelocityContext();
		VelocityContext pakcontext = new VelocityContext();
		context.put("sumLine", getTotalLine());
		context.put("sumPackage", getTotalPackage());
		context.put("sumAnalyzedLine", getTotalAnalyzedLine());
		context.put("sumCoverage", getTotalCoverage());
		context.put("sumUnCoverage", getTotalUncoverage());
		context.put("schema", schema);
		context.put("total_line", pk_line);
		context.put("analyzed_line", pk_analyzed_line);
		context.put("code_coverage", pk_coverage);
		context.put("un_code_coverage", pk_uncoverage);
		// File file = new File("report");
		// file.mkdir();
		FileOutputStream fos = new FileOutputStream("report/report.html");
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos,
				"utf-8"));
		template.merge(context, writer);
		writer.close();
		Map<String, List<String>> pksrc = po.pk_src_table;
		Set<Entry<String, List<String>>> pksrcSet = pksrc.entrySet();
		Iterator<Entry<String, List<String>>> pksrcIter = pksrcSet.iterator();
		while (pksrcIter.hasNext()) {
			Entry<String, List<String>> entry = pksrcIter.next();
			String pkname = entry.getKey();
			List<String> code_coverage = po.pk_line_cov_table.get(pkname);
			List<String> source_code = po.pk_src_code_table.get(pkname);
			List<String> line_time = po.pk_line_time_table.get(pkname);
			pakcontext.put("package_name", pkname);
			pakcontext.put("coverage_list", code_coverage);
			pakcontext.put("source_code", source_code);
			pakcontext.put("line_time", line_time);
			String fileName = "report/" + pkname + ".html";
			FileOutputStream pakfos = new FileOutputStream(fileName);
			BufferedWriter pakwriter = new BufferedWriter(
					new OutputStreamWriter(pakfos, "utf-8"));
			paktemplate.merge(pakcontext, pakwriter);
			pakwriter.close();
		}

	}

	public void report(String schema,
			Map<String, Map<String, String>> sourceCodeMap,
			int totalLine, int analyzedLine, int coveredLine, Map<String, String> pkcoverage,
			Map<String, String> pkuncoverage,Map<String, Map<String, String>> pkDetailCoverageInfo,Map<String, Map<String, String>> pkDetailTimeInfo,Map<String, Map<String, String>> pkDetailOccurInfo) {
		try{
		Properties p = new Properties();
		p.setProperty("resource.loader", "class");
		p.setProperty("class.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		p.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
		p.setProperty(Velocity.INPUT_ENCODING, "UTF-8");
		p.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
		Velocity.init(p);
		Template template = Velocity.getTemplate("details.vm");
		Template paktemplate = Velocity.getTemplate("package.vm");
		VelocityContext context = new VelocityContext();
		VelocityContext pakcontext = new VelocityContext();

		float c = analyzedLine;
		float cov = 0;
		float uncov = 0;
		cov = c / totalLine * 100;
		uncov = 100 - cov;
		context.put("sumLine", totalLine);
		context.put("sumPackage", sourceCodeMap);
		context.put("sumAnalyzedLine", analyzedLine);
		context.put("sumCoveredLine", coveredLine);
		context.put("sumCoverage", String.format("%.2f", cov));
		context.put("sumUnCoverage", String.format("%.2f", uncov));
		context.put("schema", schema);
		context.put("code_coverage", pkcoverage);
		context.put("un_code_coverage", pkuncoverage);
		context.put("code_coverage_info", pkDetailCoverageInfo);
		FileOutputStream fos = new FileOutputStream("report/report.html");
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos,
				"utf-8"));
		template.merge(context, writer);
		writer.close();
		Iterator<String> unitIter = pkDetailCoverageInfo.keySet().iterator();
		while (unitIter.hasNext()) {
			String unit = unitIter.next();
//			String pkname = entry.getKey();
			Map<String,String> coverage4unit = pkDetailCoverageInfo.get(unit);
			Map<String,String> time4unit = pkDetailTimeInfo.get(unit);
			pakcontext.put("package_name", unit);
			pakcontext.put("coverage", coverage4unit);
			pakcontext.put("sourceCode", sourceCodeMap.get(unit));
			pakcontext.put("lineTime", time4unit);
			pakcontext.put("lineTotalOccur",pkDetailOccurInfo.get(unit) );
			String fileName = "report/" + unit + ".html";
			FileOutputStream pakfos = new FileOutputStream(fileName);
			BufferedWriter pakwriter = new BufferedWriter(
					new OutputStreamWriter(pakfos, "utf-8"));
			paktemplate.merge(pakcontext, pakwriter);
			pakwriter.close();
		}

		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	public static void main(String[] args) throws Exception {
		CodeCoverageReportGenerator servlet = new CodeCoverageReportGenerator();
		Map<String, String> request = new Hashtable<String, String>();
		Map<String, String> code_coverage_table = new LinkedHashMap<String, String>();
		Map<String, String> source_code = new LinkedHashMap<String, String>();
		Map<String, String> line_time = new LinkedHashMap<String, String>();
		Map<String, String> pk_line = new LinkedHashMap<String, String>();
		Map<String, String> pk_cov = new LinkedHashMap<String, String>();
		Map<String, String> pk_analyzed_line = new LinkedHashMap<String, String>();
		pk_line.put("TMS.PK_RIGHT_COMMON", "10");
		pk_line.put("TMS.PK_RIGHT_C", "10");
		pk_cov.put("TMS.PK_RIGHT_COMMON", "10");
		pk_cov.put("TMS.PK_RIGHT_C", "10");
		pk_analyzed_line.put("TMS.PK_RIGHT_COMMON", "8");
		pk_analyzed_line.put("TMS.PK_RIGHT_C", "8");
		// request.put("schema", "TMS");
		// request.put("package_name", "TMS.PK_RIGHT_COMMON");
		// request.put("total_line", "682");
		// request.put("analyzed_line", "500");
		// request.put("coverage_percent", "43");
		// request.put("code_coverage", "60");
		code_coverage_table.put("1", "C");
		code_coverage_table.put("2", "C");
		code_coverage_table.put("3", "");
		code_coverage_table.put("4", "C");
		code_coverage_table.put("5", "");
		source_code.put("1", " o_errcode := PCN.CN_ERRCODE_INIT;");
		source_code.put("2", " o_errmsg := PCN.CN_ERRMSG_INIT;");
		source_code
				.put("3",
						" v_msg := 'sp_get_user_usergroup[�������]<' || i_systemuserid || ',' || i_rightid || '>';");
		source_code.put("4",
				" plog.debug(i_operator, i_ipaddr, i_funcid, v_msg);");
		source_code.put("5", " DELETE FROM tmp_user_usergroup;");
		line_time.put("1", "0.1");
		line_time.put("2", "0.1");
		line_time.put("3", "0.1");
		line_time.put("4", "0.1");
		line_time.put("5", "0.1");
		Map<String, String> test = new LinkedHashMap<String, String>();
		test.put("1", "TMS.PK_RIGHT_COMMON");
		test.put("2", "TMS.PK_RIGHT_COMMON");
		test.put("3", "TMS.PK_RIGHT_COMMON");
		test.put("4", "TMS.PK_RIGHT_COMMON");
		test.put("5", "TMS.PK_RIGHT_C");
		// PlsqlOperation.pk_line_table = test;
		// PlsqlOperation.source_code = source_code;
		// PlsqlOperation.pk_line_cov = code_coverage_table;
		// PlsqlOperation.pk_line_time = line_time;
		// PlsqlOperation.createPackageSrcMapping();
		// servlet.report("TMS", pk_line, pk_cov, pk_analyzed_line);
	}

}
