import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import s3exception.InvalidHeaderException;
import s3exception.InvalidParamException;
import s3exception.InvalidOpException;
import s3exception.InvalidOptionException;

public class CheckArgs {
    private ParseArgs parse = null;
    
    /*
     * Used to check whether the parameter is valid or not
     */
    private Set<String> op_set = new HashSet<String>(Arrays.asList("GetService",
                                                                   "DeleteBucket",
                                                                   "DeleteBucketlifecycle",
                                                                   "DeleteBucketwebsite",
                                                                   "GetBucket",
                                                                   "GetBucketacl",
                                                                   "GetBucketlifecycle",
                                                                   "GetBucketObjectversions",
                                                                   "GetBucketversioning",
                                                                   "GetBucketwebsite",
                                                                   "HeadBucket",
                                                                   "PutBucket",
                                                                   "PutBucketacl",
                                                                   "PutBucketlifecycle",
                                                                   "PutBucketwebsite",
                                                                   "PutBucketversioning",
                                                                   "DeleteObject",
                                                                   "DeleteMultipleObjects",
                                                                   "GetObject",
                                                                   "GetObjectacl",
                                                                   "HeadObject",
                                                                   "PutObject",
                                                                   "PutObjectacl",
                                                                   "PutObjectCopy",
                                                                   "InitiateMultipartUpload",
                                                                   "UploadPart",
                                                                   "CompleteMultipartUpload",
                                                                   "AbortMultipartUpload",
                                                                   "ListParts",
                                                                   "ListMultipartUploads"));
    
    private Set<String> comm_headers_set = new HashSet<String>(Arrays.asList("Content-Type",
                                                                             "x-amz-content-sha256"));
    
    private Set<String> get_bucket_v1_params_set = new HashSet<String>(Arrays.asList("delimiter",
                                                                                     "encoding-type",
                                                                                     "marker",
                                                                                     "max-keys",
                                                                                     "prefix"));
    
    private Set<String> get_bucket_v2_params_set = new HashSet<String>(Arrays.asList("list-type",
                                                                                     "delimiter",
                                                                                     "encoding-type",
                                                                                     "max-keys",
                                                                                     "prefix",
                                                                                     "continuation-token",
                                                                                     "fetch-owner",
                                                                                     "start-after"));
    
    private Set<String> get_bucket_object_versions_params_set = new HashSet<String>(Arrays.asList("delimiter",
                                                                                                  "encoding-type",
                                                                                                  "key-marker",
                                                                                                  "max-keys",
                                                                                                  "prefix",
                                                                                                  "version-id-marker"));
    
    /*
     * Used for check PutBucket, PutBucketacl and PutObjectacl
     */
    private Set<String> put_acl_headers_set = new HashSet<String>(Arrays.asList("x-amz-acl",
                                                                                "x-amz-grant-read",
                                                                                "x-amz-grant-write",
                                                                                "x-amz-grant-read-acp",
                                                                                "x-amz-grant-write-acp",
                                                                                "x-amz-grant-full-control"));

    private Set<String> get_object_params_set = new HashSet<String>(Arrays.asList("response-content-type",
                                                                                  "response-content-language",
                                                                                  "response-expires",
                                                                                  "response-cache-control",
                                                                                  "response-content-disposition",
                                                                                  "response-content-encoding"));
    
    /*
     * Used for check GetObject and HeadObject
     */
    private Set<String> get_object_headers_set = new HashSet<String>(Arrays.asList("Range",
                                                                                   "If-Modified-Since",
                                                                                   "If-Unmodified-Since",
                                                                                   "If-Match",
                                                                                   "If-None-Match"));
    
    private Set<String> put_object_headers_set = new HashSet<String>(Arrays.asList("Cache-Control",
                                                                                   "Content-Disposition",
                                                                                   "Content-Encoding",
                                                                                   "Content-Type",
                                                                                   "Expires",
                                                                                   "x-amz-acl",
                                                                                   "x-amz-grant-read",
                                                                                   "x-amz-grant-write",
                                                                                   "x-amz-grant-read-acp",
                                                                                   "x-amz-grant-write-acp",
                                                                                   "x-amz-grant-full-control"));
    
    private Set<String> put_object_copy_headers_set = new HashSet<String>(Arrays.asList("x-amz-copy-source",
                                                                                        "x-amz-copy-source-if-match",
                                                                                        "x-amz-copy-source-if-none-match",
                                                                                        "x-amz-copy-source-if-unmodified-since",
                                                                                        "x-amz-copy-source-if-modified-since",
                                                                                        "x-amz-acl",
                                                                                        "x-amz-grant-read",
                                                                                        "x-amz-grant-write",
                                                                                        "x-amz-grant-read-acp",
                                                                                        "x-amz-grant-write-acp",
                                                                                        "x-amz-grant-full-control"));
    
    public CheckArgs(ParseArgs parse_args) {
        parse = parse_args;
    }
    
    public void check () throws InvalidOpException, InvalidHeaderException, InvalidOptionException, InvalidParamException {
        check_basic_option();
        check_op();
        
        String op = parse.getOp_type();
        
        if (op.equalsIgnoreCase("GetService")) {
            check_get_service();
        } else if (op.equalsIgnoreCase("DeleteObject")) {
            check_delete_object();
        } else if (op.equalsIgnoreCase("DeleteBucket") ||
                   op.equalsIgnoreCase("DeleteBucketlifecycle") ||
                   op.equalsIgnoreCase("GetBucketlifecycle") ||
                   op.equalsIgnoreCase("GetBucketacl") ||
                   op.equalsIgnoreCase("GetBucketversioning") ||
                   op.equalsIgnoreCase("DeleteBucketwebsite") ||
                   op.equalsIgnoreCase("GetBucketwebsite") ||
                   op.equalsIgnoreCase("HeadBucket")) {
            check_delete_bucket();
        } else if (op.equalsIgnoreCase("GetBucket")) {
            check_get_bucket();
        } else if (op.equalsIgnoreCase("GetBucketObjectversions")) {
            check_get_bucket_object_versions();
        } else if (op.equalsIgnoreCase("PutBucket")) {
            check_put_bucket();
        } else if (op.equalsIgnoreCase("PutBucketacl")) {
            check_put_bucket_acl();
        } else if (op.equalsIgnoreCase("DeleteMultipleObjects") ||
                   op.equalsIgnoreCase("PutBucketlifecycle")) {
            check_put_bucket_lifecycle();
        } else if (op.equalsIgnoreCase("GetObject")) {
            check_get_object();
        } else if (op.equalsIgnoreCase("GetObjectacl")) {
            check_get_object_acl();
        } else if (op.equalsIgnoreCase("PutObjectacl")) {
            check_put_object_acl();
        } else if (op.equalsIgnoreCase("HeadObject")) {
            check_head_object();
        } else if (op.equalsIgnoreCase("PutObject")) {
            check_put_object();
        } else if (op.equalsIgnoreCase("PutObjectCopy")) {
        	check_put_object_copy();
        } else if (op.equalsIgnoreCase("PutBucketwebsite")) {
        	check_put_bucket_website();
        } else if (op.equalsIgnoreCase("PutBucketversioning")) {
        	check_put_bucket_versioning();
        } else if (op.equalsIgnoreCase("UploadPart")) {
        	check_upload_part();
        } else if (op.equalsIgnoreCase("CompleteMultipartUpload")) {
        	check_complete_multipart_upload();
        } else if (op.equalsIgnoreCase("AbortMultipartUpload")) {
        	check_abort_multipart_upload();
        } else if (op.equalsIgnoreCase("ListParts")) {
        	check_list_parts();
        }
    }
    
    private void check_list_parts() throws InvalidOptionException {
        if (parse.isUse_md5() ||
        	parse.isUpload_static_website() ||
        	parse.getBucket_name().isEmpty() ||
        	parse.getObject_name().isEmpty() ||
        	!parse.getFile_path().isEmpty() ||
        	!parse.getVersion_id().isEmpty() ||
        	parse.getUpload_id().isEmpty()) {
            throw new InvalidOptionException();
        }
	}

	private void check_abort_multipart_upload() throws InvalidOptionException {
        if (parse.isUse_md5() ||
        	parse.isUpload_static_website() ||
        	parse.getBucket_name().isEmpty() ||
        	parse.getObject_name().isEmpty() ||
        	!parse.getFile_path().isEmpty() ||
        	!parse.getVersion_id().isEmpty() ||
        	parse.getUpload_id().isEmpty()) {
            throw new InvalidOptionException();
        }
	}

	private void check_complete_multipart_upload() throws InvalidOptionException {
        if (parse.isUse_md5() ||
        	parse.isUpload_static_website() ||
        	parse.getBucket_name().isEmpty() ||
        	parse.getObject_name().isEmpty() ||
        	parse.getFile_path().isEmpty() ||
        	!parse.getVersion_id().isEmpty() ||
        	parse.getUpload_id().isEmpty()) {
            throw new InvalidOptionException();
        }
    }

	private void check_upload_part() throws InvalidOptionException {
        if (parse.isUse_md5() ||
        	parse.getBucket_name().isEmpty() ||
        	parse.getObject_name().isEmpty() ||
        	parse.getFile_path().isEmpty() ||
        	!parse.getVersion_id().isEmpty() ||
        	parse.getUpload_id().isEmpty()// ||
        	//parse.getPart_number() <= 0 ||
        	//parse.getPart_id() < 1 ||
        	//parse.getPart_id() > 10000 ||
        	//parse.getPart_number() < parse.getPart_id()
        	) {
            throw new InvalidOptionException();
        }
    }

	public void check_basic_option() throws InvalidOptionException {
        if (parse.getOp_type().isEmpty() ||
            parse.getHost().isEmpty()) {
            throw new InvalidOptionException();
        }
        
        if (!(parse.isAnonymous() && parse.getAccess_key().isEmpty() && parse.getSecret_key().isEmpty() ||
            !parse.isAnonymous() && !parse.getAccess_key().isEmpty() && !parse.getSecret_key().isEmpty())) {
            throw new InvalidOptionException();
        }
    }
    
    public void check_op() throws InvalidOpException {
        if (!op_set.contains(parse.getOp_type())) {
            throw new InvalidOpException();
        }
    }
    
    public void check_common_headers() throws InvalidHeaderException {
        for (Map.Entry<String, String> entry : parse.getHttp_headers().entrySet()) {
            if (!comm_headers_set.contains(entry.getKey())) {
                throw new InvalidHeaderException();
            }
        } 
    }
    
    public void check_put_bucket_lifecycle() throws InvalidOptionException {
        if (!parse.isUse_md5() ||
        	parse.getBucket_name().isEmpty() ||
        	!parse.getObject_name().isEmpty() ||
        	parse.getFile_path().isEmpty() ||
        	!parse.getVersion_id().isEmpty()) {
            throw new InvalidOptionException();
        }
    }
    
    public void check_get_service() throws InvalidHeaderException, InvalidOptionException {
        check_common_headers();
        
        if (parse.isAnonymous() ||  // can't anonymous access
            parse.isUse_md5() ||
            !parse.getBucket_name().isEmpty() || 
            !parse.getObject_name().isEmpty() ||
            !parse.getFile_path().isEmpty() ||
            !parse.getVersion_id().isEmpty()) {
            throw new InvalidOptionException();
        }
    }
    
    public void check_delete_object() throws InvalidHeaderException, InvalidOptionException {
        check_common_headers();
        
        if (parse.isUse_md5() ||
            parse.getBucket_name().isEmpty() || 
            parse.getObject_name().isEmpty() ||
            !parse.getFile_path().isEmpty()) {
            throw new InvalidOptionException();
        }
    }
    
    public void check_delete_bucket() throws InvalidHeaderException, InvalidOptionException {
        check_common_headers();
        
        if (parse.isUse_md5() ||
            parse.getBucket_name().isEmpty() || 
            !parse.getObject_name().isEmpty() ||
            !parse.getFile_path().isEmpty() ||
            !parse.getVersion_id().isEmpty()) {
            throw new InvalidOptionException();
        }
    }
    
    public void check_get_bucket() throws InvalidParamException, InvalidOptionException {
        if (parse.isUse_md5() ||
            parse.getBucket_name().isEmpty() || 
            !parse.getObject_name().isEmpty() ||
            !parse.getFile_path().isEmpty() ||
            !parse.getVersion_id().isEmpty()) {
            throw new InvalidOptionException();
        }
        
        Set<String> get_bucket_set = null;
        if (parse.getHttp_params().containsKey("list-type")) {
            get_bucket_set = get_bucket_v2_params_set;
        } else {
            get_bucket_set = get_bucket_v1_params_set;
        }
        
        for (Map.Entry<String, String> entry : parse.getHttp_params().entrySet()) {
            if (!get_bucket_set.contains(entry.getKey())) {
                throw new InvalidParamException();
            }
        }
    }
    
    public void check_get_bucket_object_versions() throws InvalidParamException, InvalidOptionException {
        if (parse.isUse_md5() ||
            parse.getBucket_name().isEmpty() || 
            !parse.getObject_name().isEmpty() ||
            !parse.getFile_path().isEmpty() ||
            !parse.getVersion_id().isEmpty()) {
            throw new InvalidOptionException();
        }
        
        for (Map.Entry<String, String> entry : parse.getHttp_params().entrySet()) {
            if (!get_bucket_object_versions_params_set.contains(entry.getKey())) {
                throw new InvalidParamException();
            }
        }
    }
    
    public void check_put_bucket() throws InvalidHeaderException, InvalidOptionException {
        if (parse.isUse_md5() ||
            parse.getBucket_name().isEmpty() || 
            !parse.getObject_name().isEmpty() ||
            !parse.getVersion_id().isEmpty()) {
            throw new InvalidOptionException();
        }
        
        for (Map.Entry<String, String> entry : parse.getHttp_headers().entrySet()) {
            if (!put_acl_headers_set.contains(entry.getKey())) {
                throw new InvalidHeaderException();
            }
        }
    }
    
    public void check_put_bucket_acl() throws InvalidHeaderException, InvalidOptionException {
        if (parse.getBucket_name().isEmpty() || 
            !parse.getObject_name().isEmpty() ||
            !parse.getVersion_id().isEmpty()) {
            throw new InvalidOptionException();
        }
        
        for (Map.Entry<String, String> entry : parse.getHttp_headers().entrySet()) {
            if (!put_acl_headers_set.contains(entry.getKey())) {
                throw new InvalidHeaderException();
            }
        }
    }
    
    public void check_get_object() throws InvalidHeaderException, InvalidParamException, InvalidOptionException {
        if (parse.isUse_md5() ||
            parse.getBucket_name().isEmpty() || 
            parse.getObject_name().isEmpty() ||
            parse.getFile_path().isEmpty()) {
            throw new InvalidOptionException();
        }
        
        for (Map.Entry<String, String> entry : parse.getHttp_params().entrySet()) {
            if (!get_object_params_set.contains(entry.getKey())) {
                //throw new InvalidParamException();
            }
        }
        
        for (Map.Entry<String, String> entry : parse.getHttp_headers().entrySet()) {
            if (!get_object_headers_set.contains(entry.getKey())) {
                //throw new InvalidHeaderException();
            }
        }
    }
    
    public void check_head_object() throws InvalidHeaderException, InvalidParamException, InvalidOptionException {
        if (parse.isUse_md5() ||
            parse.getBucket_name().isEmpty() || 
            parse.getObject_name().isEmpty() ||
            !parse.getFile_path().isEmpty()) {
            throw new InvalidOptionException();
        }
        
        for (Map.Entry<String, String> entry : parse.getHttp_headers().entrySet()) {
            if (!get_object_headers_set.contains(entry.getKey())) {
                throw new InvalidHeaderException();
            }
        }
    }
    
    public void check_put_object() throws InvalidHeaderException, InvalidOptionException {
        if (parse.getBucket_name().isEmpty() || 
            parse.getFile_path().isEmpty()) {
            throw new InvalidOptionException();
        }
        
        for (Map.Entry<String, String> entry : parse.getHttp_headers().entrySet()) {
            if (!put_object_headers_set.contains(entry.getKey())) {
                //throw new InvalidHeaderException();
            }
        }
    }
    
    public void check_get_object_acl() throws InvalidParamException, InvalidOptionException {
        if (parse.isUse_md5() ||
            parse.getBucket_name().isEmpty() || 
            parse.getObject_name().isEmpty() ||
            !parse.getFile_path().isEmpty()) {
            throw new InvalidOptionException();
        }
    }
    
    public void check_put_object_acl() throws InvalidParamException, InvalidHeaderException, InvalidOptionException {
        if (parse.isUse_md5() ||
            parse.getBucket_name().isEmpty() || 
            parse.getObject_name().isEmpty()) {
            throw new InvalidOptionException();
        }
        
        boolean put_acl_by_header = false;
        for (Map.Entry<String, String> entry : parse.getHttp_headers().entrySet()) {
            if (!put_acl_headers_set.contains(entry.getKey())) {
                throw new InvalidHeaderException();
            } else {
                put_acl_by_header = true;
            }
        }
        
        if (put_acl_by_header && !parse.getFile_path().isEmpty()) {
            throw new InvalidOptionException();
        }
    }
    
    public void check_put_object_copy() throws InvalidHeaderException, InvalidOptionException {
        if (parse.isUse_md5() ||
            parse.getBucket_name().isEmpty() || 
            parse.getObject_name().isEmpty() ||
            !parse.getFile_path().isEmpty() ||
            !parse.getVersion_id().isEmpty()) {
            throw new InvalidOptionException();
        }
        
        for (Map.Entry<String, String> entry : parse.getHttp_headers().entrySet()) {
            if (!put_object_copy_headers_set.contains(entry.getKey())) {
                throw new InvalidHeaderException();
            }
        }
    }
    
    public void check_put_bucket_website() throws InvalidOptionException {
    	if (parse.getBucket_name().isEmpty() ||
            !parse.getObject_name().isEmpty() ||
            parse.getFile_path().isEmpty() ||
            !parse.getVersion_id().isEmpty()) {
                throw new InvalidOptionException();
            }
    }
    
    public void check_put_bucket_versioning() throws InvalidOptionException {
    	if (parse.getBucket_name().isEmpty() || 
            !parse.getObject_name().isEmpty() ||
            parse.getFile_path().isEmpty() ||
            !parse.getVersion_id().isEmpty()) {
                throw new InvalidOptionException();
            }
    }
}
