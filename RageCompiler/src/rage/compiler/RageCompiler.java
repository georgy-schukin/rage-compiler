package rage.compiler;

import rage.compiler.parser.RageParser;

import java.util.ArrayList;
import java.util.List;

/*
 * Rage compiler
 */

public class RageCompiler
{
    protected final static String rageFileExtension = ".rgl";

    protected static String getLocalName(String fileName) {
        int ind = Math.max(fileName.lastIndexOf("/"), fileName.lastIndexOf("\\")); // trunc directories
        String localName = (ind != -1) ? fileName.substring(ind + 1) : fileName;
        return localName.replace(RageCompiler.rageFileExtension, "");
    }

    public static void main(String[] args) {

        List<String> files = new ArrayList<String>();
        String directory = "";

        for(int i = 0;i < args.length;i++) { // parse args
            if(args[i].equals("-d")) {
                i++;
                if(i < args.length) {
                    directory = args[i];
                }
            } else {
                files.add(args[i]);
            }
        }

        RageParser parser;
        try {
            parser = new RageParser();
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            return;
        }

        for (String inputFile : files) {
            try {
                System.out.println("Start compiling " + inputFile);
                if(parser.parse(inputFile, RageCompiler.getLocalName(inputFile), directory))
                    System.out.println("Compiling " + inputFile + " done");
                else
                    System.out.println("Failed to compile " + inputFile);
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }
}

