/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifsc.bioinfo.fast.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.steppschuh.markdowngenerator.image.Image;
import net.steppschuh.markdowngenerator.link.Link;
import net.steppschuh.markdowngenerator.list.UnorderedList;
import net.steppschuh.markdowngenerator.rule.HorizontalRule;
import net.steppschuh.markdowngenerator.table.Table;
import net.steppschuh.markdowngenerator.text.emphasis.BoldText;
import net.steppschuh.markdowngenerator.text.emphasis.ItalicText;
import net.steppschuh.markdowngenerator.text.heading.Heading;

/**
 *
 * @author renato
 */
public class MarkdownHelper {

    public static void appendHeader(StringBuilder base, String txt, int level) {
        base.append(new Heading(txt, level)).append("\n");
    }

    public static void appendList(StringBuilder base, List<Object> items) {
        base.append(new UnorderedList<>(items));
    }

    public static <K extends Object, V extends Object> void appendTableFromMap(StringBuilder base, String[] header, HashMap<K, V> map) {
        Table.Builder tableBuilder = new Table.Builder()
                .withAlignments(LEFT, LEFT)
                .addRow(header);

        for (Map.Entry entry : map.entrySet()) {
            tableBuilder.addRow(entry.getKey(), entry.getValue());
        }

        base.append(tableBuilder.build()).append("\n");
    }

    public static void appendTable(StringBuilder base, String[] header, Integer[] alignments, ArrayList<String[]> data) {
        Table.Builder tableBuilder = new Table.Builder()
                .withAlignments(alignments)
                .addRow(header);

        for (String[] strings : data) {
            tableBuilder.addRow(strings);
        }

        base.append(tableBuilder.build()).append("\n");
    }

    public static String getItalic(String txt) {
        return new ItalicText(txt).toString();
    }

    public static String getBold(String txt) {
        return new BoldText(txt).toString();
    }

    public static String getLink(String txt, String url) {
        return new Link(txt, url).toString();
    }

    public static String getLink(String url) {
        return new Link(url).toString();
    }

    public static void appendLine(StringBuilder base) {
        base.append(new HorizontalRule().toString()).append("\n");
    }

    public static void appendImage(StringBuilder base, String text, String url) {
        base.append(new Image(text, url).toString()).append("\n");
    }

    public static void main(String[] args) {
        StringBuilder res = new StringBuilder();
        appendHeader(res, "Renato", 1);

        String[] header = {"Teste 1", "Teste2"};
        Integer[] all = {CENTER, LEFT};
        ArrayList<String[]> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String[] row = {"int" + i, "val" + i};
            data.add(row);
        }
        appendTable(res, header, all, data);
        HashMap map = new HashMap<>();
        map.put("Proteínas 123123123: ", "0");
        map.put("Prots: ", "0");
        map.put("Prot123123123eínas: ", "0");
        map.put("Prote123123123s: ", "0");
        appendLine(res);
        appendTableFromMap(res, header, map);

        List lista = Arrays.asList("Teste1", "Teste2", "Teste3");
        appendList(res, lista);
        System.out.println(res);

        StringBuilder outputmd = new StringBuilder();
        MarkdownHelper.appendHeader(outputmd, "Fast-protein Software 1.0", 1);
        MarkdownHelper.appendHeader(outputmd, "Developed by PhD. Renato Simoes Moreira - "
                + MarkdownHelper.getLink("renato.simoes@ifsc.edu.br", "mailto:renato.simoes@ifsc.edu.br"), 5);
        MarkdownHelper.appendHeader(outputmd, "Protein information software", 5);
        MarkdownHelper.appendHeader(outputmd, "Pattern ER Retention [KRHQSA][DENQ][E][L] based on " + MarkdownHelper.getLink("PROSITE-PS00014", "https://prosite.expasy.org/ "), 5);
        MarkdownHelper.appendHeader(outputmd, "Pattern N-Glyc [N][Xaa(except P)][ST] Xaa = any aminoacid", 5);
        System.out.println(outputmd);

    }
    public static Integer CENTER = Table.ALIGN_CENTER;
    public static Integer RIGHT = Table.ALIGN_RIGHT;
    public static Integer LEFT = Table.ALIGN_LEFT;

}
