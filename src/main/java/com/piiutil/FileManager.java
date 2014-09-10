package com.piiutil;

import java.io.*;
import java.util.*;
import org.apache.commons.io.FileUtils;

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
public class FileManager {

    
    public static String TRANSIENT = "TRANSIENT";
    public static final String FINAL = TRANSIENT;
    
    public static void main(String[] args) throws Exception{
        TRANSIENT = "CHANGED";
        System.out.println("FINAL = "+FINAL);
        
    }

    
    public static boolean writeOut(String filename, String content){
        boolean success = false;
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
            out.println(content);
            out.close();
            success = true;
        } catch (IOException e) {
            System.out.println("Exception "+e);
        }
        return success;
    
    }
    
    public static boolean deleteFile(String targetFilePath){
        boolean success = false;
        try {
            File targetFile = new File(targetFilePath);
            FileUtils.forceDelete(targetFile);
            System.out.println("Deleted: " + targetFilePath);
            success = true;
        } catch (Exception e) {
            System.out.println("Exception while deleting " + targetFilePath);
            System.out.println(e);
        }
        return success;
    }
    
    public static boolean initialize(String targetFilePath){
        boolean success = false;
        try {
            File targetFile = new File(targetFilePath);
            FileUtils.forceDelete(targetFile);
            success = true;
        } catch (Exception e) {
        }
        
        return success;
    }
    
}
