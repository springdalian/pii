package com.piiutil;

import java.io.*;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

/**
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * @author sachin.sharma
 * 
 */
public class EncryptionManager {
    
    public static void processFile(
            List<EncryptionRule> rules,
            String inputFilePath,
            String outputDirectory,
            String outputFileName,
            String delimiter
    ) throws IOException, InvalidKeyException {
        
        List<Integer> columns = EncryptionManager.getColumnIds(rules);

        if (!outputDirectory.trim().endsWith("/")){
            outputDirectory = outputDirectory+"/";
        }    
        
        //FileManager.deleteFile(outputDirectory+outputFileName);
        
        
        BufferedReader bis = new BufferedReader(new FileReader(inputFilePath));
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputDirectory+outputFileName, true)));
        
        String line = null;
        int count = 0;

        //process lines
        while ((line = bis.readLine()) != null) {

            String hashedString = "";
            
            if (count == 0){
                String headerString = EncryptionManager.getHeaderString(line, rules, delimiter);

                if (headerString != null && headerString.length() > 0) {
                    //append header
                    out.println(headerString);
                }
                
            }else{
                hashedString = EncryptionManager.getHashedString(line, rules, delimiter);
                if (hashedString != null && hashedString.length() > 0) {
                    out.println(hashedString);
                }
            
            }

            count++;
            if ((count % 1000) == 0) {
                System.out.println(count + " records processed");
            }
        
        }
        bis.close();
        out.close();
    }
    
    
    public static String getHashedString(String line, List<EncryptionRule> rules, String delimiter) throws InvalidKeyException {
        List<Integer> columns = EncryptionManager.getColumnIds(rules);

        StringBuffer sb = new StringBuffer();
        int tokenNum = 0;
        
        String[] tokenArray = line.split(delimiter, -1);
        //make sure that line has sufficient tokens 
        validateTokenCount(tokenArray, columns);

        for(tokenNum = 0; tokenNum < tokenArray.length; tokenNum++){    
            String tokenStr = tokenArray[tokenNum];
            tokenStr = removeDoubleQuotes(tokenStr);
            //System.out.println("#" + tokenNum + ": " + tokenStr);
            if(columns.contains(new Integer(tokenNum))){
                EncryptionRule rule = getRule(tokenNum, rules);
                
                if(rule.getEncryptions().size() > 0){
                    //apply encryptions
                    List<Integer> encryptions = rule.getEncryptions();
                    for(Integer encryption : encryptions){
                        if(tokenStr != null && tokenStr.trim().length() > 0){
                            if(encryption == EncryptionRule.SHA1){
                                sb.append(getSHA1Enc(tokenStr)).append(delimiter);
                                //System.out.println("    Encrypt #" + tokenNum + " SHA1 , Token: " + tokenStr+" hashed: "+getSHA1Enc(tokenStr));
                            }else if(encryption == EncryptionRule.SHA256){
                                sb.append(getSHA256Enc(tokenStr)).append(delimiter);
                                //System.out.println("    Encrypt #" + tokenNum + " SHA256 , Token: " + tokenStr+" hashed: "+getSHA256Enc(tokenStr));
                            }
                        }else{
                            //String was blank - just leave blank instead of encrypting
                            sb.append("").append(delimiter);
                        }
                        
                    }
                }else{
                    //no encryptions to apply
                    sb.append(tokenStr).append(delimiter);
                    //System.out.println("Token #" + tokenNum + ", Token: " + tokenStr);
                }
                
            }else{
                //TODO APPEND TOKEN AS_IS
                sb.append(tokenStr).append(delimiter);
            }
            
        }
        
        //remove the last comma if end reached
        String result = sb.toString();
        result = result.substring(0, result.lastIndexOf(delimiter));
        
        return result;
    }

    public static String getHeaderString(String line, List<EncryptionRule> rules, String delimiter) throws InvalidKeyException {
        List<Integer> columns = EncryptionManager.getColumnIds(rules);

        StringBuffer sb = new StringBuffer();
        int tokenNum = 0;
        
        String[] tokenArray = line.split(delimiter, -1);
        //make sure that line has sufficient tokens 
        validateTokenCount(tokenArray, columns);

        for(tokenNum = 0; tokenNum < tokenArray.length; tokenNum++){    
            String tokenStr = tokenArray[tokenNum];
            tokenStr = removeDoubleQuotes(tokenStr);
            //System.out.println("#" + tokenNum + ": " + tokenStr);
            if(columns.contains(new Integer(tokenNum))){
                EncryptionRule rule = getRule(tokenNum, rules);
                
                if(rule.getEncryptions().size() > 0){
                    //apply encryptions
                    List<Integer> encryptions = rule.getEncryptions();
                    for(Integer encryption : encryptions){
                        if(tokenStr != null && tokenStr.trim().length() > 0){
                            if(encryption == EncryptionRule.SHA1){
                                sb.append(tokenStr+PiiConstants.SHA1_SUFFIX).append(delimiter);
                                //System.out.println("    Encrypt #" + tokenNum + " SHA1 , Token: " + tokenStr+" hashed: "+getSHA1Enc(tokenStr));
                            }else if(encryption == EncryptionRule.SHA256){
                                sb.append(tokenStr+PiiConstants.SHA256_SUFFIX).append(delimiter);
                                //System.out.println("    Encrypt #" + tokenNum + " SHA256 , Token: " + tokenStr+" hashed: "+getSHA256Enc(tokenStr));
                            }
                        }else{
                            //String was blank - just leave blank instead of encrypting
                            sb.append("").append(delimiter);
                        }
                        
                    }
                }else{
                    //no encryptions to apply
                    sb.append(tokenStr).append(delimiter);
                    //System.out.println("Token #" + tokenNum + ", Token: " + tokenStr);
                }
                
            }else{
                //TODO APPEND TOKEN AS_IS
                sb.append(tokenStr).append(delimiter);
            }
            
        }
        
        //remove the last comma if end reached
        String result = sb.toString();
        result = result.substring(0, result.lastIndexOf(delimiter));
        
        return result;
    }
    
    
    public static void printEncryptionRules(List<EncryptionRule> rules) {
        System.out.println("---- File Processing and Encryption Rules---");
        int counter = 0;
        for (EncryptionRule rule : rules) {
            counter++;
            System.out.println(counter+") columnId: " + rule.getColumnId()+" columnName:" + rule.getColumnName() );
            for (Integer enc : rule.getEncryptions()) {
                if (enc == EncryptionRule.SHA1) {
                    System.out.println("    encryption: " + EncryptionRule.SHA1_KEY);
                } else if (enc == EncryptionRule.SHA2) {
                    System.out.println("    encryption: " + EncryptionRule.SHA2_KEY);
                } else if (enc == EncryptionRule.SHA256) {
                    System.out.println("    encryption: " + EncryptionRule.SHA256_KEY);
                }else{
                    System.out.println("    encryption: NONE");
                }
            }
        }
        System.out.println("---- End of Rules---");
    }
    
    public static List<EncryptionRule> getDefaultEncryptionRules() throws FileNotFoundException, IOException, InvalidArgumentException{
        System.out.println("getDefaultEncryptionRules()");

        List<EncryptionRule> rules = new ArrayList<EncryptionRule>();
        EncryptionRule rule  = new EncryptionRule();
        rule.setColumnName("PK");
        List<Integer> encRulesList = new ArrayList<Integer>();
        encRulesList.add(EncryptionRule.SHA1);
        encRulesList.add(EncryptionRule.SHA256);
        rule.setEncryptions(encRulesList);
        rules.add(rule);

        return rules;

    }


    public static List<EncryptionRule> getEncryptionRules(String fileName) throws FileNotFoundException, IOException, InvalidArgumentException{
        System.out.println("loading EncryptionRules from: " + fileName);

        List<EncryptionRule> rules = new ArrayList<EncryptionRule>();

        BufferedReader in = null;
        StringTokenizer token = null;
        int lineNum = 0, tokenNum = 0;

        in = new BufferedReader(new FileReader(fileName));
        String str;
        while ((str = in.readLine()) != null) {
            lineNum++;
            token = new StringTokenizer(str, PiiConstants.RULES_DELIMITER);
            EncryptionRule rule  = new EncryptionRule();
            List<Integer> encRulesList = new ArrayList<Integer>();
            while (token.hasMoreTokens()) {
                tokenNum++;
                String tokenValue = token.nextToken();
                //System.out.println("Line #" + lineNum + ", Token #" + tokenNum + ", Token: " + tokenValue);
                if (tokenNum == 1) {
                    rule.setColumnName(tokenValue);

                } else if (tokenNum == 2) {
                    rule.setColumnId(Integer.parseInt(tokenValue));
                } else if (tokenNum > 2) {
                    if (tokenValue != null){
                        if (tokenValue.trim().equalsIgnoreCase(EncryptionRule.SHA1_KEY)) {
                            encRulesList.add(EncryptionRule.SHA1);
                        } else if (tokenValue.trim().equalsIgnoreCase(EncryptionRule.SHA2_KEY)) {
                            encRulesList.add(EncryptionRule.SHA2);
                        } else if (tokenValue.trim().equalsIgnoreCase(EncryptionRule.SHA256_KEY)) {
                            encRulesList.add(EncryptionRule.SHA256);
                        }else{
                            throw new InvalidArgumentException("Invalid hash config file: ambiguous encryption name on line["+lineNum+"] :: "+str);
                        }
                    }
                }
            }
            tokenNum = 0;
            rule.setEncryptions(encRulesList);
            rules.add(rule);
        }
        in.close();

        return rules;

    }

    
    public static List<Integer> getColumnIds(List<EncryptionRule> rules) {
        List<Integer> columns = new ArrayList<Integer>();
        for (EncryptionRule rule : rules){
            columns.add(rule.getColumnId());
        }
        return columns;
    }

    public static EncryptionRule getRule(Integer id, List<EncryptionRule> rules) {
        EncryptionRule result = null;
        for (EncryptionRule rule : rules){
            if(id == rule.getColumnId()){
                result = rule;
                break;
            }
        }
        return result;
    }
    
    public static String getSHA1Enc(String str){
        String hashed = "";
        try{
            hashed = HashDigest.hash(str, EncryptionRule.SHA1);
        }catch(Exception e){
            System.out.println("Exception while hashing: "+str);
        }
        return hashed;
    }
    public static String getSHA2Enc(String str){
        String hashed = "";
        try{
            hashed = HashDigest.hash(str, EncryptionRule.SHA2);
        }catch(Exception e){
            System.out.println("Exception while hashing: "+str);
        }
        return hashed;
    }
    public static String getSHA256Enc(String str){
        String hashed = "";
        try{
            hashed = HashDigest.hash(str, EncryptionRule.SHA256);
        }catch(Exception e){
            System.out.println("Exception while hashing: "+str);
        }
        return hashed;
    }

    public static String purify(String str){
        String pureStr = "";
        if(str != null){
            pureStr = str.trim();
        }
        return pureStr;
    }
    
    public static void validateTokenCount(String[] tokenArray, List<Integer> columns) throws InvalidKeyException {
        if(tokenArray.length < columns.size()){
            throw new InvalidKeyException("The input file is not compatible with the configuration file (specified by -conf) and the delimiter (specified by –delm).");
        }
    }

    public static String removeDoubleQuotes(String tokenStr) {
        //remove starting and ending double quotes
        tokenStr = tokenStr.trim();
        if(tokenStr.startsWith("\"")){
            tokenStr = tokenStr.substring(1);
        }
        if(tokenStr.endsWith("\"")){
            tokenStr = tokenStr.substring(0, tokenStr.length() -1);
        }
        return tokenStr;
    }
    
}
