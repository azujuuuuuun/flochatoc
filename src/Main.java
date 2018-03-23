import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);

        System.out.println("このプログラムは、フローチャートのXMLファイルからC言語のプログラムを作成します。");
        System.out.print("XMLのファイル名を入力してください。：");
        String inputFileName = sc.next();
        if (!inputFileName.endsWith(".xml")) {
            inputFileName += ".xml";
        }
        String inputFilePath = "in/" + inputFileName;
        File inputFile = new File(inputFilePath);
        if (!inputFile.exists()) {
            System.out.println(inputFileName + "は存在しません。");
            System.exit(1);
        }

        ActionState actionStates[] = ActionStateMaker.analyze(inputFilePath);

        String[] program = CProgramMaker.create(actionStates);

        String outputFileName = inputFileName.replace("xml", "c");
        String outputFilePath = "out/" + outputFileName;
        File outputFile = new File(outputFilePath);
        if (outputFile.exists()) {
            System.out.print(outputFileName + "は既に存在しています。上書きしますか？（y/n）");
            String answer = sc.next();
            if (!answer.equals("y")) {
                return ;
            }
        } else {
            try {
                if (outputFile.createNewFile()) {
                    System.out.println(outputFileName + "の作成に成功しました。");
                } else {
                    System.out.println(outputFileName + "の作成に失敗しました。");
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        try {
            PrintWriter pw = new PrintWriter(outputFilePath);
            for (int i = 0; i < program.length; i++) {
                pw.println(program[i]);
            }
            pw.close();
        } catch (IOException e) {
            System.out.print(e);
        }
    }
}