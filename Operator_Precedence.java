import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Operator_Precedence {
    /* 文法 */
    private static Map<Character, List<String> > grammar = new HashMap<Character, List<String>>(){
        {
            List<String> s1=new ArrayList<>();
            s1.add("N+N");s1.add("N*N");s1.add("(N)");s1.add("i");
            /*
            List<String> s2=new ArrayList<>();
            s2.add("T*F");s2.add("F");
            List<String> s3=new ArrayList<>();
            s3.add("(E)");s3.add("i");
            */
            put('N',s1);
            /*
            put('T',s2);
            put('F',s3);
             */
        }
    };
    /* 终结符 */
    private static Set<Character> Vt = new LinkedHashSet<Character>(){
        {
            add('+');add('*');add('(');add(')');add('i');add('#');
        }
    };
    /* 非终结符 */
    private static Set<Character> Vn = new LinkedHashSet<Character>(){
        {
            add('N');
            //  add('E');add('T');add('F');
        }
    };
    /* 优先关系矩阵 */
    private static Map<String, Character> matrix = new HashMap<String, Character>(){
        {
            put("++",'>');put("+*",'<');put("+(",'<');put("+)",'>');put("+i",'<');put("+#",'>');
            put("*+",'>');put("**",'>');put("*(",'<');put("*)",'>');put("*i",'<');put("*#",'>');
            put("(+",'<');put("(*",'<');put("((",'<');put("()",'=');put("(i",'<');put("(#",' ');
            put(")+",'>');put(")*",'>');put(")(",' ');put("))",'>');put(")i",' ');put(")#",'>');
            put("i+",'>');put("i*",'>');put("i(",' ');put("i)",'>');put("ii",' ');put("i#",'>');
            put("#+",'<');put("#*",'<');put("#(",'<');put("#)",' ');put("#i",'<');put("##",' ');
        }
    };
    /* 规约 */
    public static char reduce(int start,List<Character> a){
        StringBuilder str=new StringBuilder();
        for(int i=start;i<a.size();i++){
            str.append(a.get(i));
        }
        //System.out.println("规约："+str);
        for (Map.Entry<Character, List<String>> map : grammar.entrySet()){
            for(String get: map.getValue()){
                if(!get.equals(str.toString())){
                }else{
                    return map.getKey();
                }
            }
        }
        return 0;
    }

    public static void analysis(StringBuilder input){
        char a=' ';char Q=' ';
        int i=0;
        int k=0;
        int j=0;
        int flag=0;
        List<Character> SymbolStack=new ArrayList<>();
        input.append('#');
        SymbolStack.add('#');
        while (a!='#'&&flag==0){
            a=input.charAt(i++);
            //System.out.println("a:"+a);
            //System.out.println("k:"+SymbolStack.get(k));
            //中缀表达式，Vn一定与Vt交叉相邻，得到栈顶终结符的位置
            if(Vt.contains(SymbolStack.get(k))){
                j=k;
            }else{
                j=k-1;
            }

            if(matrix.get(SymbolStack.get(j).toString()+a)!=null) {
                if(matrix.get(SymbolStack.get(j).toString()+a)==' '){
                    System.out.println("E");
                    break;
                }
                while (matrix.get(SymbolStack.get(j).toString() + a) == '>') {
                    do {
                        Q = SymbolStack.get(j);
                        if (Vt.contains(SymbolStack.get(j - 1))) {
                            j = j - 1;
                        } else {
                            j = j - 2;
                        }
                    } while (matrix.get(SymbolStack.get(j).toString() + Q) == '>' || matrix.get(SymbolStack.get(j).toString() + Q) == '=');
                    /*规约*/
                    char N = reduce(j + 1, SymbolStack);
                    if (N == 0||matrix.get(SymbolStack.get(j).toString() + Q) == ' ') {
                        flag = 1;
                        System.out.println("RE");
                        break;
                    } else {
                        System.out.println("R");
                        k = j + 1;
                        SymbolStack = SymbolStack.subList(0, k);
                        SymbolStack.add(N);
                        System.out.println(SymbolStack.toString());
                    }

                }

                if((SymbolStack.get(j)=='#'&&a=='#')||flag==1){
                    break;
                }
                if((matrix.get(SymbolStack.get(j).toString()+a)=='<'||matrix.get(SymbolStack.get(j).toString()+a)=='=')){
                    k=k+1;
                    if(SymbolStack.size()<=k){
                        SymbolStack.add(a);
                    }else {
                        SymbolStack.set(k, a);
                    }
                    System.out.println("I"+a);
                }
            }
        }
    }

    public static String readFileByLines(String fileName) {
        File file = new File(fileName);
        StringBuilder str=new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                // str.append(' ');
                str.append(tempString);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }

    public static void main(String[] args) {

        StringBuilder input=new StringBuilder();
        input.append(readFileByLines(args[0]));
        // System.out.println(input);
        analysis(input);
    }
}
