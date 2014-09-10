package com.piiutil;

import java.util.*;

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
public class EncryptionRule {
    public static Integer SHA1 = 1;
    public static Integer SHA2 = 2;
    public static Integer SHA256 = 3;
    public static String SHA1_KEY = "sha1";
    public static String SHA2_KEY = "sha2";
    public static String SHA256_KEY = "sha256";
    
    String columnName = "";
    Integer columnId = 0;
    List<Integer> encryptions = new ArrayList<Integer>();
    
    //value for appendages
    String columnValue = "";

    public EncryptionRule() {
    }
    
    public EncryptionRule(Integer columnId) {
        setColumnId(columnId);
    }
    
    public EncryptionRule(Integer columnId, List<Integer> encryptions) {
        setColumnId(columnId);
        setEncryptions(encryptions);
    }
    
    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Integer getColumnId() {
        return columnId;
    }

    public void setColumnId(Integer columnId) {
        this.columnId = columnId;
    }

    public List<Integer> getEncryptions() {
        return encryptions;
    }

    public void setEncryptions(List<Integer> encryptions) {
        this.encryptions = encryptions;
    }
    
    public String getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(String columnValue) {
        this.columnValue = columnValue;
    }
    
    
}
