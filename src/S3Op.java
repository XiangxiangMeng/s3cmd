import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import s3exception.InvalidParamException;

public class S3Op {
    private String url_text = "";
    private String op_type = "";   //"HEAD", "GET", "PUT", "DELETE", "POST"
    private String request_uri = ""; // like "/Bucket/", "/Bucket/object" or "/Bucket/object?acl", etc
    private String canonicalized_amz_headers_str = "";
    private String signature_str = "";
    private String sub_resource = "";
    private Map<String, File> files_map;
    private ParseArgs parse = null;
    private boolean has_params = false;
    private boolean has_subresource = false;
    private Map<String, String> x_amz_http_headers = null;
    
    /* used for http request header */
    private String http_MD5 = "";
    private String http_content_type = "";
    private String http_authorization = "";
    private String http_date = "";
    
    private HttpRequestBase http_request = null;
    private final static String HMAC_SHA1 = "HmacSHA1";

    /* content type */
    private Map<String, Set<String>> mime_types_map;
    
    public S3Op(ParseArgs parse) {
        this.parse = parse;
        x_amz_http_headers = new TreeMap<>();
        files_map = new TreeMap<>();
        mime_types_map = new TreeMap<>();
    }

    public void init() throws NoSuchAlgorithmException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException, InvalidKeyException  {      
    	get_op();
        gen_sub_resource();
        open_files();
        init_mime_types();
        gen_canonicalized_amz_headers();
        gen_canonicalized_amz_headers_str();
    }

    private void init_mime_types() {
    	Set<String> mime_msexcel = new HashSet<>();
    	mime_msexcel.add(".xls");
    	mime_msexcel.add(".xla");
    	mime_types_map.put("application/msexcel", mime_msexcel);
    	
    	Set<String> mime_mshelp = new HashSet<>();
    	mime_mshelp.add(".hlp");
    	mime_mshelp.add(".chm");
    	mime_types_map.put("application/mshelp", mime_mshelp);
    	
    	Set<String> mime_mspowerpoint = new HashSet<>();
    	mime_mspowerpoint.add(".ppt");
    	mime_mspowerpoint.add(".ppz");
    	mime_mspowerpoint.add(".pps");
    	mime_mspowerpoint.add(".pot");
    	mime_types_map.put("application/mspowerpoint", mime_mspowerpoint);
    	
    	Set<String> mime_msword = new HashSet<>();
    	mime_msword.add(".doc");
    	mime_msword.add(".dot");
    	mime_types_map.put("application/msword", mime_msword);
    	
    	Set<String> mime_pdf = new HashSet<>();
    	mime_pdf.add(".pdf");
    	mime_types_map.put("application/pdf", mime_pdf);
    	
    	Set<String> mime_rtf = new HashSet<>();
    	mime_rtf.add(".rtf");
    	mime_types_map.put("application/rtf", mime_rtf);
    	
    	Set<String> mime_php = new HashSet<>();
    	mime_php.add(".php");
    	mime_php.add(".phtml");
    	mime_types_map.put("application/x-httpd-php", mime_php);
    	
    	Set<String> mime_flash = new HashSet<>();
    	mime_flash.add(".swf");
    	mime_flash.add(".cab");
    	mime_types_map.put("application/x-shockwave-flash", mime_flash);
    	
    	Set<String> mime_zip = new HashSet<>();
    	mime_zip.add(".zip");
    	mime_types_map.put("application/zip", mime_zip);
    	
    	Set<String> mime_eot = new HashSet<>();
    	mime_eot.add(".eot");
    	mime_types_map.put("application/vnd.ms-fontobject", mime_eot);
    	
    	Set<String> mime_svg = new HashSet<>();
    	mime_svg.add(".svg");
    	mime_svg.add(".svgz");
    	mime_types_map.put("image/svg+xml", mime_svg);
    	
    	Set<String> mime_ttf = new HashSet<>();
    	mime_ttf.add(".ttc");
    	mime_ttf.add(".ttf");
    	mime_types_map.put("application/x-font-ttf", mime_ttf);
    	
    	Set<String> mime_woff = new HashSet<>();
    	mime_woff.add(".woff");
    	mime_types_map.put("application/font-woff", mime_woff);
    	
    	Set<String> mime_auto_basic = new HashSet<>();
    	mime_auto_basic.add(".au");
    	mime_auto_basic.add(".snd");
    	mime_types_map.put("audio/basic", mime_auto_basic);
    	
    	Set<String> mime_auto_mpeg = new HashSet<>();
    	mime_auto_mpeg.add(".mp3");
    	mime_types_map.put("audio/mpeg", mime_auto_mpeg);
    	
    	Set<String> mime_wav = new HashSet<>();
    	mime_wav.add(".wav");
    	mime_types_map.put("audio/x-wav", mime_wav);
    	
    	Set<String> mime_jpeg = new HashSet<>();
    	mime_jpeg.add(".jpeg");
    	mime_jpeg.add(".jpg");
    	mime_jpeg.add(".jpe");
    	mime_types_map.put("image/jpeg", mime_jpeg);
    	
    	Set<String> mime_png = new HashSet<>();
    	mime_png.add(".png");
    	mime_types_map.put("image/png", mime_png);
    	
    	Set<String> mime_gif = new HashSet<>();
    	mime_gif.add(".gif");
    	mime_types_map.put("image/gif", mime_gif);
    	
    	Set<String> mime_text_plain = new HashSet<>();
    	mime_text_plain.add(".txt");
    	mime_types_map.put("text/plain", mime_text_plain);
    	
    	Set<String> mime_css = new HashSet<>();
    	mime_css.add(".css");
    	mime_types_map.put("text/css", mime_css);
    	
    	Set<String> mime_json = new HashSet<>();
    	mime_json.add(".json");
    	mime_types_map.put("application/json", mime_json);
    	
    	Set<String> mime_html = new HashSet<>();
    	mime_html.add(".htm");
    	mime_html.add(".html");
    	mime_html.add(".shtml");
    	mime_types_map.put("text/html", mime_html);
    	
    	Set<String> mime_javascript = new HashSet<>();
    	mime_javascript.add(".js");
    	mime_types_map.put("application/javascript", mime_javascript);
    	
    	Set<String> mime_mp4 = new HashSet<>();
    	mime_mp4.add(".mp4");
    	mime_types_map.put("video/mp4", mime_mp4);
    	
    	Set<String> mime_ogv = new HashSet<>();
    	mime_ogv.add(".ogv");
    	mime_types_map.put("video/ogg", mime_ogv);
    	
    	Set<String> mime_webm = new HashSet<>();
    	mime_webm.add(".webm");
    	mime_types_map.put("video/webm", mime_webm);
	}

	private void gen_sub_resource() {
        sub_resource = "";
        String op_type = parse.getOp_type();
        if (op_type.equalsIgnoreCase("PutBucketlifecycle") ||
            op_type.equalsIgnoreCase("GetBucketlifecycle") ||
            op_type.equalsIgnoreCase("DeleteBucketlifecycle")) {
            sub_resource = "lifecycle";
        } else if (op_type.equalsIgnoreCase("PutBucketacl") ||
                   op_type.equalsIgnoreCase("GetBucketacl") ||
                   op_type.equalsIgnoreCase("PutObjectacl") ||
                   op_type.equalsIgnoreCase("GetObjectacl")) {
            sub_resource = "acl";
        } else if (op_type.equalsIgnoreCase("PutBucketversioning") ||
                   op_type.equalsIgnoreCase("GetBucketversioning")) {
            sub_resource = "versioning";
        } else if (op_type.equalsIgnoreCase("GetBucketObjectversions")) {
            sub_resource = "versions";
        } else if (op_type.equalsIgnoreCase("DeleteMultipleObjects")) {
            sub_resource = "delete";
        } else if (op_type.equalsIgnoreCase("PutBucketwebsite") ||
        		   op_type.equalsIgnoreCase("GetBucketwebsite") ||
        		   op_type.equalsIgnoreCase("DeleteBucketwebsite")) {
        	sub_resource = "website";
        }
    }
    
    /* generate the format of below:
     * 		/
     * 		/Bucket/      // this format used for calc signature
     * 		/Bucket/?acl  // this format used for calc signature in virtual-hosted-style, otherwise you should use /Bucket?acl
     * 		/Bucket/object
     * 		/Bucket/object?acl
     * */
	private void gen_request_uri(String filename) {
        request_uri = "/";
        
        if (!parse.getBucket_name().isEmpty()) {
            request_uri += parse.getBucket_name();
            if (filename.isEmpty()) {
                request_uri += "/";
            }
        } else {
        	return;
        }
    	
        if (!filename.isEmpty()) {
            request_uri += ("/" + filename);
        }
        
        if (!sub_resource.isEmpty()) {
            request_uri += ("?" + sub_resource);
            has_subresource = true;
        }
    }
    
	private void gen_url_text() {
        url_text = "http://";
 
    	int pos = request_uri.indexOf('/', 1);
    	if (pos > 0) {
    		if (parse.isVirtual_hosted_style()) {
    			String vt_request_uri = request_uri.substring(pos);   // remove the bucket part from request_uri
        		pos =  vt_request_uri.indexOf('?');
        		if (pos == 1) {
        			vt_request_uri = vt_request_uri.substring(pos);   // transfer "/?acl" to "?acl"
        		}
        		url_text += (parse.getBucket_name() + "." + parse.getHost() + vt_request_uri);
    		} else {
    			String bucket_str = request_uri.substring(0, pos);
    			String tmp_str = request_uri.substring(pos);
    			pos =  tmp_str.indexOf('?');
        		if (pos == 1) {
        			tmp_str = tmp_str.substring(pos);   // transfer "/?acl" to "?acl"
        		}
        		request_uri =  bucket_str + tmp_str;
    			url_text += (parse.getHost() + request_uri);
    		}
    	} else {
    		url_text += (parse.getHost() + "/");
    		return;    // GetService
    	}
                
        if (!parse.getHttp_params().isEmpty()) {
            if (!has_subresource) {
                url_text += "?";
                has_params = true;
            } else {
                url_text += "&";
            }
            
            int size = parse.getHttp_params().size();
            for (Map.Entry<String, String> entry : parse.getHttp_params().entrySet()) {
                url_text += (entry.getKey() + "=" + entry.getValue());
                
                if(--size > 0) {
                    url_text += "&";
                }
            }
        }
        
        if (!parse.getVersion_id().isEmpty()) {
            if (!has_subresource && !has_params) {
                url_text += "?";
            } else {
                url_text += "&";
            }
            
            url_text += ("versionId=" + parse.getVersion_id());
        }
    }
    
	private void open_files() throws IOException {
        String file_path = parse.getFile_path();
        String path;
        File file;
        File[] files;
        
        if(!file_path.isEmpty()) {
        	LinkedList<String> folderList = new LinkedList<String>();
        	if (file_path.endsWith("/")) {
        		file_path = file_path.substring(0, file_path.length() - 1);
    		}
    		folderList.add(file_path); 
    		while (folderList.size() > 0) {
    			file = new File(folderList.peek());
    		    folderList.removeFirst();
    		    files = file.listFiles();
    		    // file_path is not a dir
    		    if (files == null) {
    		    	path = file.getAbsolutePath();
    		    	if (parse.isUpload_static_website()) {
    		    		files_map.put(path.substring(file_path.lastIndexOf('/') + 1), file);
    		    	} else {
    		    		files_map.put(path.substring(1), file);
    		    	}
    		    	break;
    		    } else if (!parse.getOp_type().equals("PutObject")) {
    		    	throw new IOException("file path can not be dir!!!");
    		    }
    		    for (int i = 0; i < files.length; i++) {
    		    	file = files[i];
    		        if (file.isDirectory()) {
    		            folderList.add(file.getPath());
    		        } else {
    		        	//files_map.put(file.getAbsolutePath().substring(file_path.lastIndexOf('/') + 1), file.getAbsoluteFile());
    		        	path = file.getAbsolutePath();
    		        	if (parse.isUpload_static_website()) {
    		        		files_map.put(path.substring(file_path.length() + 1), file.getAbsoluteFile());	// remove the whole path
    		        	} else {
    		        		files_map.put(path.substring(1), file.getAbsoluteFile());  // remove the first '/'
    		        	}
    		        }
    		    }
    		}
        } else {
        	// In order to make files_map general
        	files_map.put("", null);
        }
    }
    
	private void gen_md5(File file) throws NoSuchAlgorithmException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
    	if (parse.isUse_md5() && file != null && file.canRead()) {
            http_MD5 = getFileMD5(file);
        }
    }
    
	private void get_op() {
        String op = parse.getOp_type();
        if (op.equalsIgnoreCase("GetService") ||
            op.equalsIgnoreCase("GetBucket") ||
            op.equalsIgnoreCase("GetBucketacl") ||
            op.equalsIgnoreCase("GetBucketlifecycle") ||
            op.equalsIgnoreCase("GetBucketObjectversions") ||
            op.equalsIgnoreCase("GetBucketversioning") ||
            op.equalsIgnoreCase("GetBucketwebsite") ||
            op.equalsIgnoreCase("GetObject") ||
            op.equalsIgnoreCase("GetObjectacl")) {
            op_type = "GET";
        } else if (op.equalsIgnoreCase("PutBucket") ||
        		   op.equalsIgnoreCase("PutBucketacl") ||
        		   op.equalsIgnoreCase("PutBucketlifecycle") ||
        		   op.equalsIgnoreCase("PutBucketwebsite") ||
        		   op.equalsIgnoreCase("PutBucketversioning") ||
        		   op.equalsIgnoreCase("PutObject") ||
        		   op.equalsIgnoreCase("PutObjectacl") ||
        		   op.equalsIgnoreCase("PutObjectCopy")) {
            op_type = "PUT";
        } else if (op.equalsIgnoreCase("HeadBucket") ||
                   op.equalsIgnoreCase("HeadObject")) {
            op_type = "HEAD";
        } else if (op.equalsIgnoreCase("DeleteBucket") ||
                   op.equalsIgnoreCase("DeleteBucketlifecycle") ||
                   op.equalsIgnoreCase("DeleteBucketwebsite") ||
                   op.equalsIgnoreCase("DeleteObject")) {
            op_type = "DELETE";
        } else if (op.equalsIgnoreCase("DeleteMultipleObjects")) {
            op_type = "POST";
        }
    }
    
	private boolean has_content_type() {
        for (Map.Entry<String, String> entry : parse.getHttp_headers().entrySet()) {
            if (entry.getKey().equalsIgnoreCase("Content-Type")) {
                http_content_type = entry.getValue();
                return true;
            }
        }
        
        return false;
    }
    
	private void gen_date() {
        http_date = getGMTTime();
    }
    
	private void gen_canonicalized_amz_headers_map() {
        for (Map.Entry<String, String> entry : parse.getHttp_headers().entrySet()) {
            String key = entry.getKey().toLowerCase();
            String value = entry.getValue();
            String new_value;
            
            if (key.substring(0, 6).equalsIgnoreCase("x-amz-")) {
                if (x_amz_http_headers.containsKey(key)) {
                	// x-amz-test:aaa, x-amz-test:bbb  ===>  x-amz-test:aaa,bbb
                    new_value = x_amz_http_headers.get(key);
                    new_value += ("," + value);
                    x_amz_http_headers.put(key, new_value);
                } else {
                    x_amz_http_headers.put(key, value);
                }
            }
        }
    }
    
	private void gen_canonicalized_amz_headers_str() {
        gen_canonicalized_amz_headers_map();
        
        canonicalized_amz_headers_str = "";
        
        for (Map.Entry<String, String> entry : x_amz_http_headers.entrySet()) {
            canonicalized_amz_headers_str += (entry.getKey() + ":" + entry.getValue() + "\n");
        }
    }
    
	private void gen_canonicalized_resource_params_map() {
		
	}
	
	private void gen_signature_str() {
        //gen_canonicalized_amz_headers_str();
        signature_str = "";
        
        signature_str += (op_type + "\n");
        
        if (!http_MD5.isEmpty()) {
            signature_str += (http_MD5 + "\n");
        } else {
            signature_str += "\n";
        }
        
        if (!http_content_type.isEmpty()) {
            signature_str += (http_content_type + "\n");
        } else {
            signature_str += "\n";
        }
        
        if (!http_date.isEmpty()) {
            signature_str += (http_date + "\n");
        } else {
            signature_str += "\n";
        }
        
        if (!canonicalized_amz_headers_str.isEmpty()) {
            signature_str += canonicalized_amz_headers_str;
        }
        
        // for /bucket/, amazon need the last /, but it should be /bucket in ceph
        int pos = request_uri.lastIndexOf("/", 1);
    	if (pos > 0) {
    		request_uri = request_uri.substring(0, pos);
    	}
    	
        signature_str += request_uri;
    }
    
	private void gen_authorization() throws InvalidKeyException, UnsupportedEncodingException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchAlgorithmException{
        if (parse.isAnonymous()) {
            return;
        }
        
        http_authorization = "AWS " + parse.getAccess_key() + ":" + getSignature(parse.getSecret_key(), signature_str);
    }
    
	private void gen_http_request() throws URISyntaxException {
    	if (http_request != null) {
    		http_request.reset();
    		http_request.setURI(new URI(url_text));
    		return;
    	}
    	
        if (op_type.equals("GET")) {
            http_request = new HttpGet(url_text);
        } else if (op_type.equals("PUT")) {
            http_request = new HttpPut(url_text);
        } else if (op_type.equals("HEAD")) {
            http_request = new HttpHead(url_text);
        } else if (op_type.equals("POST")) {
            http_request = new HttpPost(url_text);
        } else if (op_type.equals("DELETE")) {
            http_request = new HttpDelete(url_text);
        } 
    }
    
	private void process_http_headers_and_body(File file) {
        if (parse.isVirtual_hosted_style()) {
        	String bucket = parse.getBucket_name();
        	if (!bucket.isEmpty()) {
        		bucket += ".";
        	}
            http_request.setHeader("Host", bucket + parse.getHost());
        }
        
        if (!http_date.isEmpty()) {
            http_request.setHeader("Date", http_date);
        }
        
        if (!http_MD5.isEmpty()) {
            http_request.setHeader("Content-MD5", http_MD5);
        }
        
        if (!http_content_type.isEmpty()) {
            http_request.setHeader("Content-Type", http_content_type);
        }
        
        if (!http_authorization.isEmpty()) {
            http_request.setHeader("Authorization", http_authorization);
        }
        
        for (Map.Entry<String, String> entry : parse.getHttp_headers().entrySet()) {
            http_request.setHeader(entry.getKey(), entry.getValue());
        }
        
        if (file !=null && file.canRead()) {
            HttpEntity reqEntity = new FileEntity(file);
            if (op_type.equals("PUT")) {
                ((HttpPut) http_request).setEntity(reqEntity);
            } else if (op_type.equals("POST")) {
                ((HttpPost) http_request).setEntity(reqEntity);
            }
        }
    }
    
    private String re_get_object_key(String object_key) {
    	
    	// PutObjectacl with xml (object is not null and file is not null)
    	if (!parse.getObject_name().isEmpty() && !object_key.isEmpty()) {
    		object_key = parse.getObject_name();
    	} else {
    		String op = parse.getOp_type();
    		
    		if (op.equals("PutBucketacl") ||
    			op.equals("PutBucketversioning") ||
    			op.equals("PutBucketwebsite") ||
    			op.equals("PutBucketlifecycle") ||
    			op.equals("DeleteMultipleObjects")) {
    			// object is null and file is not null
    			object_key = "";
    		} else if (op.equals("GetObject") ||
        			   op.equals("HeadObject") ||
        			   op.equals("DeleteObject") ||
        			   op.equals("GetObjectacl") ||
        			   op.equals("PutObjectacl") ||		//PutObjectacl with HTTP Header
        			   op.equals("PutObjectCopy")) {
    			// object is not null and file is null
    			object_key = parse.getObject_name();
    		}
    	}
    	
    	return object_key;
    }
    
    private void gen_content_type(String file_name)
    {
    	if (!parse.getOp_type().equals("PutObject") || file_name.isEmpty() || has_content_type()) {
    		return;
    	}
    	
    	String content_type;
    	Set<String> mime_set;
    	String extension;
    	int pos;
    	
    	pos = file_name.lastIndexOf('.');
    	if (pos == -1) {
    		return;
    	}
    	
    	extension = file_name.substring(pos);
    	
    	for (Map.Entry<String, Set<String>> entry : mime_types_map.entrySet()) {
    		content_type = entry.getKey();
    		mime_set = entry.getValue();
    		
    		if (mime_set.contains(extension)) {
    			http_content_type = content_type;
    			return;
    		}
    	}
    }
    
    public void process_request() throws Exception {
        String object_key;
        File file;
                
        for (Map.Entry<String, File> entry : files_map.entrySet()) {
        	object_key = entry.getKey();
        	file = entry.getValue();
        	
        	object_key = re_get_object_key(object_key);
        	
            gen_content_type(object_key);
        	gen_request_uri(object_key);
            gen_url_text();
            gen_http_request();
            gen_md5(file);
            gen_date();
            gen_signature_str();
            gen_authorization();
            
            process_http_headers_and_body(file);
            
            CloseableHttpClient http_client = HttpClients.createDefault();
            
            CloseableHttpResponse response = http_client.execute(http_request);
            
            HttpEntity entity = response.getEntity();
            
            if (entity != null) {
                long resp_len = entity.getContentLength();
                if (resp_len > 0) {
                    System.out.println(EntityUtils.toString(entity));
                }
            }            
        }
    }

    private String encodeBase64(byte[] input) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<?> clazz = Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
        Method mainMethod = clazz.getMethod("encode", byte[].class);
        mainMethod.setAccessible(true);
        Object retObj = mainMethod.invoke(null, new Object[] { input });
        
        return (String) retObj;
    }

    private byte[] getSignature(byte[] data, byte[] key) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec signingKey = new SecretKeySpec(key, HMAC_SHA1);
        Mac mac = Mac.getInstance(HMAC_SHA1);
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(data);
        
        return rawHmac;
    }

    private String getSignature(String skey, String str) throws UnsupportedEncodingException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InvalidKeyException, NoSuchAlgorithmException {
        String u8_data = new String(str.getBytes(), "UTF-8");
        byte[] rawHmac = getSignature(u8_data.getBytes(), skey.getBytes());
        String base64 = encodeBase64(rawHmac);

        return base64;
    }

    private String getFileMD5(File file) throws NoSuchAlgorithmException, IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (!file.isFile()) {
            return "";
        }
        
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        
        digest = MessageDigest.getInstance("MD5");
        in = new FileInputStream(file);
        while ((len = in.read(buffer, 0, 1024)) != -1) {
            digest.update(buffer, 0, len);
        }
        
        in.close();

        String md5 = null;
        md5 = encodeBase64(digest.digest());

        return md5;
    }
    
    private String getGMTTime() {
        Calendar cd = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT")); // 设置时区为GMT
        String str = sdf.format(cd.getTime());
        return str;
    }
}