package com.piiutil;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.Security;

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
public class HashDigest {

    public static void main(String[] args) {

        try {
            System.out.println("yourname@emailhost.com SHA1 = " + HashDigest.hash("yourname@emailhost.com", EncryptionRule.SHA1));
            System.out.println("yourname@emailhost.com SHA256 = : " + HashDigest.hash("yourname@emailhost.com", EncryptionRule.SHA256));
            System.out.println("SHA2 -Hash: " + HashDigest.hash("test", EncryptionRule.SHA2));
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }
    public static String hash(String input, int type) throws Exception {
        String hashed = "";
        if(input != null){
            input = input.toLowerCase().trim();
            if(type == EncryptionRule.SHA1){
                hashed = makeSHA1Hash(input);
            }else if(type == EncryptionRule.SHA2){
                hashed = makeSHA256Hash(input);
            }else if(type == EncryptionRule.SHA256){
                hashed = makeSHA256Hash(input);
            }
        }
        return hashed;
    }    
    
    private static String makeSHA1Hash(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.reset();
        byte[] buffer = input.getBytes();
        md.update(buffer);
        byte[] digest = md.digest();

        String hexStr = "";
        for (int i = 0; i < digest.length; i++) {
            hexStr += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1);
        }
        return hexStr;
    }


    private static String makeSHA256Hash(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(input.getBytes());

        byte byteData[] = md.digest();

        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            String hex = Integer.toHexString(0xff & byteData[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }

}
