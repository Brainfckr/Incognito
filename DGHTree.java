package Incognito;

// import dataFly.DGHNode;

import javax.crypto.spec.DHGenParameterSpec;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

/**
 *
 * @author Dunni
 */
public class DGHTree 
{
    DGHNode root = new DGHNode();//which should always be set to {****}
    //ArrayList<DGHNode> children = new ArrayList<>();//add every possible child in the tree
    DGHNode current = new DGHNode();
    private int height = 0;
    private double weight = 0;
    private String label = "";
    
    public DGHTree(){
        
    }
    
    public DGHTree(DGHNode root){
        this.root = root;
    }

    public DGHTree(String file) throws FileNotFoundException{
        readIn(file);
    }
    
    public void setWeight(double x){
        weight = x;
    }
    
    public double getWeight(){
        return weight;
    }
    
    public void setLabel(String x){
        label = x;
    }
    
    public String getLabel(){
        return label;
    }
    
    /**
     * Read in the hierarchy into the tree
     * @param infile 
     * @throws FileNotFoundException
     */
    private void readIn(String infile) throws FileNotFoundException{
        File file = new File(infile);
        /*System.out.println("File Exists: "+file.exists());
        System.out.println("File Can Read: "+file.canRead()); */
        Scanner scan = new Scanner(file);
        String[] rootLine = scan.nextLine().split(",");
        /*for(int y = 0; y < rootLine.length; y++){
        System.out.print(rootLine[y] + " ");
        }*/
        //System.out.println();
        root.setData(rootLine[0]);
        for(int i = 1; i < rootLine.length; i++){
                DGHNode child = new DGHNode(rootLine[i]);
                child.setParent(root);
                root.children.add(child);
            }
        
        while(scan.hasNextLine()){
            String[] line = scan.nextLine().split(",");
            String parent = line[0];
            //System.out.println("Parent: " +parent);
            for(int i = 1; i < line.length; i++){
            //assuming a potential parent used is a child on the tree already except root
                search(root, parent);
                /*System.out.println(current.isNull());
                System.out.println("current-data: " +current.data);*/
                DGHNode child = new DGHNode(line[i]);
                child.setParent(current);
                current.children.add(child);
                //System.out.println("current-data: " +current.data);
            }
        }
    scan.close();    
    }
    
    /**
     * Search a DGH tree for data from the root 
     * @param treeRoot
     * @param data 
     */
    public void search(DGHNode treeRoot, String data){
        
        for(int i = 0; i < treeRoot.children.size(); i++){
            search(treeRoot.children.get(i), data);
        }
        if(treeRoot.getData().equalsIgnoreCase(data)){
            // System.out.println("I got here! " + treeRoot.data);
            //System.out.println(treeRoot.isNull());
            //newNode = treeRoot.copy();
            current = treeRoot;
        }
        
    }
    
    /**
     * Search a DGHTree to see the parent/generalized value of data
     * @param data
     * @return 
     */
    public String getGeneralization(String data){
        search(root, data);
        if(data.equalsIgnoreCase(root.getData())){
            return root.getData();
        }
        //System.out.println("data & current.parent.getData(): " + data + " & " + current.parent.getData());
        //System.out.println();
        return current.parent.getData();
    }
    
    /**
     * Print a tree from the root
     * @param roots
     * @param j - root level 
     */
    public static void printTree(DGHNode roots, int j){
        System.out.print(roots.data + " " + j + "," + " level - " +roots.level + " :");
        j++;
        for(int i = 0; i < roots.children.size(); i++){
            printTree(roots.children.get(i), j);
        }
        //System.out.println();
    }
    
    /**
     * Set the node levels in a tree
     * @param roots
     * @param j 
     */
    public void setDGHNodeLevels(DGHNode roots, int j){
        roots.setLevel(j);
        j--;
        for(int i = 0; i < roots.children.size(); i++){
            setDGHNodeLevels(roots.children.get(i), j);
        }
    }
    
    /**
     * Height of Tree
     * @return 
     */
    public int getHeight(){    
        return height;
    }
    
    /**
     * Counts from root to leaf and sets value to height
     */
    public void setHeight(){
        if(!root.isNull()){
            height = 1;
        }
        DGHNode node = root;    
        while(!node.isLeaf()){
            height++;
            int max = node.children.get(0).getChildCount();
            node = node.children.get(0);
            for(int i = 1; i < node.children.size(); i++){
                if(node.children.get(i).getChildCount() > max){
                    max = node.children.get(i).getChildCount();
                    node = node.children.get(i);
                    
                }
            }
            
        }
    }

    public static DGHTree createDGHTreeNumericRange(ArrayList<String> values) throws FileNotFoundException{
        // System.out.println("bin da ");
        ArrayList<Integer> intValues = new ArrayList<>();
        ArrayList<String> ranges = new ArrayList<>();

        if(values.size() == 0){
            return new DGHTree(new DGHNode("****"));
        }

        for (String value : values){
            //int intValue = Integer.parseInt(value.substring(0, value.length() - 2)); for numeric values -2: ".0"
            int intValue = Integer.parseInt(value);
            if (!intValues.contains(intValue)){
                intValues.add(intValue);
            }
        }

        int min = Collections.min(intValues);
        int max = Collections.max(intValues);
        int noOfRanges = (max - min)/10;
        if(((max - min)%10) != 0){
            noOfRanges ++;
        }
        // System.out.println("min, max, #ranges" + min + " " + max + " " + noOfRanges);

        for(int i = 0; i < noOfRanges; i++){
            String range = min + "-" + (min + 9);
            ranges.add(range);
            //System.out.println("noOfRange " + i + " : "+ range);
            min = min + 10;
        }
        //add ranges to DGHTree, we are starting from the root
        DGHTree tree = new DGHTree(new DGHNode("****"));
        for (String range : ranges) {
            DGHNode node = new DGHNode(range);
            // System.out.println("noOfRange " + i + " : "+ node.getData());
            node.setParent(tree.root);
            tree.root.children.add(node);
        }
        //add individual values as children to ranges
        for(int i = 0; i < tree.root.children.size(); i++){
            //System.out.println("Tree Root Children - " + tree.root.children.size());
            for (Integer value : intValues) {
                String range2 = tree.root.children.get(i).getData();
                //System.out.println("range - " + range2);
                String[] minMax = range2.split("-");
                if (value >= Integer.parseInt(minMax[0])) {
                    if (value <= Integer.parseInt(minMax[1])) {
                        DGHNode node = new DGHNode(String.valueOf(value));
                        node.setParent(new DGHNode(range2));
                        tree.root.children.get(i).children.add(node);
                    }
                }
            }
        }
        String dghFile = "/home/bob/IdeaProjects/Incognito/src/Incognito/createdDGHTree" + max + ".txt";
        File infile = new File(dghFile);
        PrintWriter writer = new PrintWriter(infile);
        ArrayList<DGHNode> valueNodes = new ArrayList<>();
        getLeaves(tree.root, valueNodes);
        for (DGHNode test : valueNodes) {
                while (test != null) {
                    //System.out.print(test.data + ",");
                    writer.print(test.data + ",");
                    test = test.parent;
                }
                //System.out.println();
                writer.println();
        }
        writer.close();
        // printTree(tree.root, 0);
        return tree;
    }

    /**
     * Creates a DGH Tree for numeric data
     * E.g from 1234 -> 123* -> 12** -> 1*** -> ****
     * @param values - list of ungeneralized raw string values
     * @return
     * @throws FileNotFoundException 
     */
    public static DGHTree createDGHTree(ArrayList<String> values) throws FileNotFoundException{
        /*I will call a method that makes sure the length of each value in values is the same */
        //ArrayList<String> newValues = rectifyValueLength(values);
        DGHTree tree = new DGHTree();
        ArrayList<DGHNode> valueNodes = new ArrayList<>();
        //ArrayList<ArrayList> valueNodeParents = new ArrayList<>();

        for (String value : values) {
            valueNodes.add(new DGHNode(value));
        }
        for (DGHNode valueNode : valueNodes) {
            setAllParents(valueNode, valueNode.getData().length() - 1);
        }
            /*for(int i = 0; i < valueNodes.size(); i++){
            valueNodeParents.add(new ArrayList<DGHNode>());
            }*/
            String dghFile = "/home/bob/IdeaProjects/Incognito/src/Incognito/createdDGHTree.txt";
            File infile = new File(dghFile);
            PrintWriter writer = new PrintWriter(infile);
        for (DGHNode test : valueNodes) {
            while (test != null) {
                //System.out.print(test.data + ",");
                writer.print(test.data + ",");
                test = test.parent;
            }
            //System.out.println();
            writer.println();
        }
            writer.close();
            
            Scanner scanner = new Scanner(infile);            
            String[] rootLine = scanner.nextLine().split(",");
            /*for(int y = 0; y < rootLine.length; y++){
            System.out.print(rootLine[y] + " ");
            }*/
            //System.out.println();
            //set Up Root
            tree.root.setData(rootLine[rootLine.length-1]);
            DGHNode child = new DGHNode(rootLine[rootLine.length-2]);
            child.setParent(tree.root);
            tree.root.children.add(child);
            /*for(int i = rootLine.length-1; i >= 0; i--){
                    DGHNode child = new DGHNode(rootLine[rootLine.length-1]);
                    child.setParent(root);
                    root.children.add(child);
                }*/
            for(int i = rootLine.length - 2; i > 0; i--){
                tree.search(tree.root, rootLine[i]);
                DGHNode testChild = new DGHNode(rootLine[i-1]);
                testChild.setParent(tree.current);
                tree.current.children.add(testChild);
            }
        
            while(scanner.hasNextLine()){
                String[] line = scanner.nextLine().split(",");
                for(int i = line.length-1; i > 0; i--){
                    tree.search(tree.root, line[i]);
                    DGHNode testChild = new DGHNode(line[i-1]);
                    testChild.setParent(tree.current);
                    if(!tree.current.hasChild(testChild))
                    {
                    tree.current.children.add(testChild);
                    }
                }
            }
        
       scanner.close();
       return tree;  
    }
    
    /**
     * Dates must be in format (YYYY-MM-DD)
     * Creates a DGH Tree for list of dates
     * E.g from YYYY-MM-DD -> YYYY-MM -> YYYY - > ****
     * @param dates - dates to be generalized
     * @return DGH tree
     * @throws FileNotFoundException
     */
    public static DGHTree createDGHTreeForDate(ArrayList<String> dates) throws FileNotFoundException{
        DGHTree tree = new DGHTree();
        ArrayList<DGHNode> dateNodes = new ArrayList<>();

        for (String date : dates) {
            dateNodes.add(new DGHNode(date));
        }
        for (DGHNode dateNode : dateNodes) {
            setAllParentsForDates(dateNode, 3);//3 for each and **** for root
        }
            
            String dghFile = "/Users/adenugad/NetBeansProjects/kAnonAlgorithms/src/datafly/createdDGHTree.txt";
            File infile = new File(dghFile);
            PrintWriter writer = new PrintWriter(infile);
        for (DGHNode test : dateNodes) {
            while (test != null) {
                //System.out.print(test.data + ",");
                writer.print(test.data + ",");
                test = test.parent;
            }
            //System.out.println();
            writer.println();
        }
            writer.close();
            
            Scanner scanner = new Scanner(infile);            
            String[] rootLine = scanner.nextLine().split(",");
            
            //set Up Root
            tree.root.setData(rootLine[rootLine.length-1]);
            DGHNode child = new DGHNode(rootLine[rootLine.length-2]);
            child.setParent(tree.root);
            tree.root.children.add(child);
          
            for(int i = rootLine.length - 2; i > 0; i--){
                tree.search(tree.root, rootLine[i]);
                DGHNode testChild = new DGHNode(rootLine[i-1]);
                testChild.setParent(tree.current);
                tree.current.children.add(testChild);
            }
        
            while(scanner.hasNextLine()){
                String[] line = scanner.nextLine().split(",");
                for(int i = line.length-1; i > 0; i--){
                    tree.search(tree.root, line[i]);
                    DGHNode testChild = new DGHNode(line[i-1]);
                    testChild.setParent(tree.current);
                    if(!tree.current.hasChild(testChild))
                    {
                    tree.current.children.add(testChild);
                    }
                }
            }
        
       scanner.close();
       return tree;  
    }
    
    /**
     * Dates must be in format (YYYY-MM-DD)
     * Creates a DGH Tree for list of dates
     * E.g from YYYY-MM-DD -> YYYY-MM -> YYYY - > 10 year range -> ****
     * @param dates - dates to be generalized
     * @return DGH tree
     */
    public DGHTree createRangesDatesDGHTrees(ArrayList<String> dates){
        //form year ranges so i only first 4
        ArrayList<Integer> years = new ArrayList<>();
        ArrayList<String> yearRanges = new ArrayList<>();
        for (String date : dates) {
            String year = date.substring(0, 4);
            if (!years.contains(Integer.parseInt(year))) {
                years.add(Integer.parseInt(year));
            }
        }
        //find min and max
        int min = years.get(0), max = years.get(0);
        //int minValueColumn = 0, maxValueColumn = 0;
        for(int i = 1; i < years.size(); i++){
            if(years.get(i) > max){
                max = years.get(i);
                //maxValueColumn = i;
            }
            if(years.get(i)< min){
                min = years.get(i);
                //minValueColumn = i;
            }
        }
        int noOfRanges = (max - min)/10;
        if(((max - min)%10) != 0){
            noOfRanges ++;
        }
        
        for(int i = 0; i < noOfRanges; i++){
            String range = min + "-" + (min + 9);
            yearRanges.add(range);
            //System.out.println("noOfRange " + i + " : "+ range);
            min = min + 10;
        }
        //add ranges to DGHTree, we starting from the root
        DGHTree tree = new DGHTree(new DGHNode("****"));
        //System.out.println("Year Range size - " + yearRanges.size());
        for (String yearRange : yearRanges) {
            DGHNode node = new DGHNode(yearRange);
            // System.out.println("noOfRange " + i + " : "+ node.getData());
            node.setParent(tree.root);
            tree.root.children.add(node);
        }
        //add individual years as children to year ranges
        for(int i = 0; i < tree.root.children.size(); i++){
            //System.out.println("Tree Root Children - " + tree.root.children.size());
            for (Integer year : years) {
                String range2 = tree.root.children.get(i).getData();
                //System.out.println("range - " + range2);
                if (year >= Integer.parseInt(range2.substring(0, 4))) {
                    if (year <= Integer.parseInt(range2.substring(5))) {
                        DGHNode node = new DGHNode(String.valueOf(year));
                        node.setParent(new DGHNode(range2));
                        tree.root.children.get(i).children.add(node);
                    }
                }
            }
        }
        //add year/mon as children to individual years - cancel duplication
        for(int i = 0; i < tree.root.children.size(); i++){
            for(int j = 0; j < tree.root.children.get(i).getChildCount(); j++){
                for (String date : dates) {
                    if (Integer.parseInt(date.substring(0, 4)) ==
                            Integer.parseInt(tree.root.children.get(i).children.get(j).getData())) {
                        DGHNode node = new DGHNode(date.substring(0, 7));
                        if (!tree.root.children.get(i).children.get(j).hasChild(node)) {
                            node.setParent(tree.root.children.get(i).children.get(j));
                            tree.root.children.get(i).children.get(j).children.add(node);
                        }
                    }
                }
            }
        }
        //add full dates(year - mon - day) as children to year-mon
        for(int i = 0; i < tree.root.children.size(); i++){
            for(int j = 0; j < tree.root.children.get(i).getChildCount(); j++){
                for(int k = 0; k < tree.root.children.get(i).children.get(j).getChildCount(); k++){
                    for (String date : dates) {
                        if (date.substring(0, 7).equalsIgnoreCase(tree.root.children.get(i).children.get(j).children.get(k).getData())) {
                            DGHNode node = new DGHNode(date);
                            node.setParent(tree.root.children.get(i).children.get(j).children.get(k).getData());
                            tree.root.children.get(i).children.get(j).children.get(k).children.add(node);
                        }
                    }
                }
            }
        }
        
    return tree;    
    }
    
    /**
     * Recursively sets a node's parent until it gets to ****
     * E.g For node 1987, parent is 198* whose parent is 19**
     * Node is numeric data
     * @param node
     * @param i = number of digits in data
     */
    public static void setAllParents(DGHNode node, int i){
        if(i >= 0){
            node.setParent(modifyString(node.getData(), i));
            i--;
            node.parent.children.add(node);
            setAllParents(node.parent, i);
        }
    }

    public static void setAllParentsNumericRange(DGHNode node, int i){
        if(i >= 0){
            node.setParent(modifyString(node.getData(), i));
            i--;
            node.parent.children.add(node);
            setAllParents(node.parent, i);
        }
    }
    
    /**
     * Recursively sets a node's parent until it gets to ****
     * Node is date
     * @param node
     * @param i = number of digits in data
     */
    public static void setAllParentsForDates(DGHNode node, int i){
        if(i > 0){
            node.setParent(modifyToDate(node.getData()));
            i--;
            node.parent.children.add(node);
            setAllParentsForDates(node.parent, i);
        }
    }
    
    public static String modifyString(String string, int level){
        String result = string.substring(0, level);
        if(level == 0){
            result = "";
        }
        for(int i = result.length(); i < string.length(); i++){
            result = result + "*";
            //System.out.println("result-" + result);
        }
        return result;
    }
    
    public static String modifyToDate(String string){
        if(string.length() == 4){
            return "****";
        }
        return string.substring(0, string.length()-3);
    }
            
    /*
    For testing purposes
    */
    public static void main(String[] args) throws FileNotFoundException {
        /*DGHTree tree = new DGHTree();
        tree.readIn("/Users/adenugad/NetBeansProjects/kAnonAlgorithms/src/datafly/dghtest.txt");
        tree.printTree(tree.root, 0);
        System.out.println();
        System.out.println(tree.getGeneralization("cough"));*/
        DGHTree tree2 = new DGHTree();
        
        
        //new ArrayList<Integer>(Arrays.asList(1,2,3,5,8,13,21));
        ArrayList<String> values = new ArrayList<>(Arrays.asList("1995-10-30", "1983-11-25", "1995-12-03","2000-07-16", "1986-11-25","1997-02-01", "2000-07-09", "2010-09-07","2011-05-05"));
        tree2 = tree2.createRangesDatesDGHTrees(values);
        DGHTree tree3 = new DGHTree("/Users/adenugad/NetBeansProjects/kAnonAlgorithms/src/datafly/" + "dghSex");
        DGHTree tree4 = new DGHTree("/Users/adenugad/NetBeansProjects/kAnonAlgorithms/src/datafly/" + "dghRace");
        
        tree2.setHeight();
        tree3.setHeight();
        tree4.setHeight();
        
        System.out.println("Tree Height2: " + tree2.getHeight());
        System.out.println("Tree Height3: " + tree3.getHeight());
        System.out.println("Tree Height4: " + tree4.getHeight());
        
        tree2.setDGHNodeLevels(tree2.root, tree2.getHeight()-1);
        tree3.setDGHNodeLevels(tree3.root, tree3.getHeight()-1);
        tree4.setDGHNodeLevels(tree4.root, tree4.getHeight()-1);
        
        tree2.printTree(tree2.root, 0);
        System.out.println();
        tree3.printTree(tree3.root, 0);
        System.out.println();
        tree4.printTree(tree4.root, 0);
        
        /*System.out.println();
        System.out.println("2000-07-09: " + tree2.getGeneralization("2000-07-09"));
        System.out.println("2000-07-16: " + tree2.getGeneralization("2000-07-16"));
        System.out.println("2000-07: " + tree2.getGeneralization("2000-07"));
        System.out.println("2000: " + tree2.getGeneralization("2000"));*/


        //System.out.println();
        /*tree2.search(tree2.root, "2000");
        for(int i = 0; i < tree2.current.getChildCount(); i++){
            System.out.println("child " + i + " -" + tree2.current.children.get(i).getData());
        }*/
        //System.out.println("Tree Height: " + tree2.getHeight());
    }

    public static void getLeaves(DGHNode root, ArrayList<DGHNode> leaves){
        if (root.isLeaf()){
            leaves.add(root);
            return;
        }
        for (DGHNode child : root.getChildren()){
            getLeaves(child, leaves);
        }
    }
}
