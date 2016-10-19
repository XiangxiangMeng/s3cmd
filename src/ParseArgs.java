import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import s3exception.InvalidHeaderException;
import s3exception.InvalidOpException;
import s3exception.InvalidOptionException;
import s3exception.InvalidParamException;

public class ParseArgs {
    /*
     * store command line parameters
     */
    private String host = "";
    private String op_type = "";
    private String access_key = "";
    private String secret_key = "";
    private String bucket_name = "";
    private String object_name = "";
    private String file_path = "";
    private String version_id = "";
    private boolean anonymous = false;
    private boolean use_md5 = false;
    private boolean upload_static_website = false;
    private boolean virtual_hosted_style = false;
    private Map<String, String> http_headers;
    private Map<String, String> http_params;

    private CommandLineParser parser = null;
    private Options options = null;
    private CommandLine commandLine = null;
    private HelpFormatter formatter = null;
    
    private CheckArgs arg_check = null;
    
    public void init() {
        options.addOption(Option.builder().longOpt("help")
                                          .desc("Print this usage information")
                                          .hasArg(false)
                                          .build());
        
        options.addOption(Option.builder().longOpt("host")
                                          .desc("server ip and port")
                                          .hasArg()
                                          .numberOfArgs(1)
                                          .argName("ip:port")
                                          .build());

        options.addOption(Option.builder().longOpt("anonymous")
                                          .desc("whether anonymous access or not")
                                          .hasArg(false)
                                          .build());

        options.addOption(Option.builder().longOpt("md5")
                                          .desc("whether use MD5 header when upload file, etc")
                                          .hasArg(false)
                                          .build());
        
        options.addOption(Option.builder().longOpt("website")
						                  .desc("upload a static website, combine with PutObject")
						                  .hasArg(false)
						                  .build());
        
        options.addOption(Option.builder().longOpt("vh-style")
							              .desc("use virtual hosted-style access method")
							              .hasArg(false)
							              .build());
        
        options.addOption(Option.builder().longOpt("type")
                                          .desc("s3 operation type:\n"
                                                  + "<GetService>\n"
                                                  + "<DeleteBucket>\n"
                                                  + "<DeleteBucketlifecycle>\n"
                                                  + "<DeleteBucketwebsite>\n"
                                                  + "<GetBucket>\n"
                                                  + "<GetBucketacl>\n"
                                                  + "<GetBucketlifecycle>\n"
                                                  + "<GetBucketwebsite>\n"
                                                  + "<GetBucketObjectversions>\n"
                                                  + "<GetBucketversioning>\n"
                                                  + "<HeadBucket>\n"
                                                  + "<PutBucket>\n"
                                                  + "<PutBucketacl>\n"
                                                  + "<PutBucketlifecycle>\n"
                                                  + "<PutBucketwebsite>\n"
                                                  + "<PutBucketversioning>\n"
                                                  + "<DeleteObject>\n"
                                                  + "<DeleteMultipleObjects>\n"
                                                  + "<GetObject>\n"
                                                  + "<GetObjectacl>\n"
                                                  + "<HeadObject>\n"
                                                  + "<PutObject>\n"
                                                  + "<PutObjectacl>\n"
                                                  + "<PutObjectCopy>")
                                          .hasArg()
                                          .numberOfArgs(1)
                                          .argName("s3 op")
                                          .build());
        
        options.addOption(Option.builder().longOpt("access-key")
                                          .desc("access key")
                                          .hasArg()
                                          .numberOfArgs(1)
                                          .argName("key")
                                          .build());
        
        options.addOption(Option.builder().longOpt("secret-key")
                                          .desc("secret key")
                                          .hasArg()
                                          .numberOfArgs(1)
                                          .argName("key")
                                          .build());
        
        options.addOption(Option.builder().longOpt("bucket")
                                          .desc("bucket name")
                                          .hasArg()
                                          .numberOfArgs(1)
                                          .argName("bucket")
                                          .build());
        
        options.addOption(Option.builder().longOpt("object")
                                          .desc("object key except upload object")
                                          .hasArg()
                                          .numberOfArgs(1)
                                          .argName("object")
                                          .build());
        
        options.addOption(Option.builder().longOpt("file")
                                          .desc("input file path (file or dir)")
                                          .hasArg()
                                          .numberOfArgs(1)
                                          .argName("file")
                                          .build());
        
        options.addOption(Option.builder().longOpt("version-id")
                                          .desc("object version id")
                                          .hasArg()
                                          .numberOfArgs(1)
                                          .argName("versionid")
                                          .build());
        
        options.addOption(Option.builder("P").desc("http request parameters")
                                              .hasArgs()
                                              .argName("param-name=value")
                                              .build());
        
        options.addOption(Option.builder("H").desc("HTTP header")
                                             .hasArgs()
                                             .argName("http-header=value")
                                             .build());
        
    }

    public ParseArgs() {
        setHttp_headers(new HashMap<>());
        setHttp_params(new HashMap<>());
        parser = new DefaultParser();
        options = new Options();
        formatter = new HelpFormatter();
        arg_check = new CheckArgs(this);
    }

    public void printUsage() {
        formatter.setLongOptSeparator("=");
        formatter.printHelp("s3cmd", options);
    }

    public void parse(String[] args) throws ParseException, InvalidOpException, InvalidHeaderException, InvalidOptionException, InvalidParamException {
        commandLine = parser.parse(options, args);
        
        if (commandLine.hasOption("help")) {
            printUsage();
            System.exit(0);
        }
        
        if (commandLine.hasOption("host")) {
            setHost(commandLine.getOptionValue("host"));
        }
        
        if (commandLine.hasOption("anonymous")) {
            setAnonymous(true);
        }
        
        if (commandLine.hasOption("md5")) {
            setUse_md5(true);
        }
        
        if (commandLine.hasOption("website")) {
            setUpload_static_website(true);
        }
        
        if (commandLine.hasOption("vh-style")) {
            this.setVirtual_hosted_style(true);
        }
        
        if (commandLine.hasOption("type")) {
            setOp_type(commandLine.getOptionValue("type"));
        }
        
        if (commandLine.hasOption("access-key")) {
            setAccess_key(commandLine.getOptionValue("access-key"));
        }
        
        if (commandLine.hasOption("secret-key")) {
            setSecret_key(commandLine.getOptionValue("secret-key"));
        }
        
        if (commandLine.hasOption("bucket")) {
            setBucket_name(commandLine.getOptionValue("bucket"));
        }
        
        if (commandLine.hasOption("object")) {
            setObject_name(commandLine.getOptionValue("object"));
        }
        
        if (commandLine.hasOption("file")) {
            setFile_path(commandLine.getOptionValue("file"));
        }
        
        if (commandLine.hasOption("version-id")) {
            setVersion_id(commandLine.getOptionValue("version-id"));
        }
        
        if (commandLine.hasOption("H")) {
            String[] headers = commandLine.getOptionValues("H");
            for(int i = 0; i <= headers.length - 2; i += 2) {
                http_headers.put(headers[i], headers[i + 1]);
            }
        }
        
        if (commandLine.hasOption("P")) {
            String[] params = commandLine.getOptionValues("P");
            String name;
            String value;
            for(int i = 0; i <= params.length - 2; i += 2) {
            	name = params[i];
            	value = params[i + 1];
            	if (name.equalsIgnoreCase("continuation-token")) {
            		//value.replaceAll("\\\\+", "%2B");
            	}
                http_params.put(name, value);
            }
        }
        
        arg_check.check();
    }
    
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getOp_type() {
        return op_type;
    }

    public void setOp_type(String op_type) {
        this.op_type = op_type;
    }

    public String getAccess_key() {
        return access_key;
    }

    public void setAccess_key(String access_key) {
        this.access_key = access_key;
    }

    public String getSecret_key() {
        return secret_key;
    }

    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
    }

    public String getBucket_name() {
        return bucket_name;
    }

    public void setBucket_name(String bucket_name) {
        this.bucket_name = bucket_name;
    }

    public String getObject_name() {
        return object_name;
    }

    public void setObject_name(String object_name) {
        this.object_name = object_name;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getVersion_id() {
        return version_id;
    }

    public void setVersion_id(String version_id) {
        this.version_id = version_id;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public boolean isUse_md5() {
        return use_md5;
    }

    public void setUse_md5(boolean use_md5) {
        this.use_md5 = use_md5;
    }

    public boolean isUpload_static_website() {
		return upload_static_website;
	}

	public void setUpload_static_website(boolean upload_static_website) {
		this.upload_static_website = upload_static_website;
	}

	public boolean isVirtual_hosted_style() {
		return virtual_hosted_style;
	}

	public void setVirtual_hosted_style(boolean virtual_hosted_style) {
		this.virtual_hosted_style = virtual_hosted_style;
	}

	public Map<String, String> getHttp_headers() {
        return http_headers;
    }

    public void setHttp_headers(Map<String, String> http_headers) {
        this.http_headers = http_headers;
    }

    public Map<String, String> getHttp_params() {
        return http_params;
    }

    public void setHttp_params(Map<String, String> http_params) {
        this.http_params = http_params;
    }

}