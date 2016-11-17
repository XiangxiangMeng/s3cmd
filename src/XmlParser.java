import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlParser {

	public void parse(String op, String respXML, boolean isFormat) throws ParserConfigurationException, SAXException, IOException {
		System.out.println("=====================================================================================");
		switch (op) {
		case "GetService":
			parseGetServiceResp(respXML, isFormat);
			break;
		case "PutObjectCopy":
			parsePutObjectCopyResp(respXML, isFormat);
			break;
		case "GetObjectacl":
			parseGetBucketAndObjectaclResp(respXML, isFormat);
			break;
		case "DeleteMultipleObjects":
			parseDeleteMultipleObjectsResp(respXML, isFormat);
			break;
		case "GetBucketacl":
			parseGetBucketAndObjectaclResp(respXML, isFormat);
			break;
		case "GetBucketlifecycle":
			parseGetBucketlifecycleResp(respXML, isFormat);
			break;
		case "GetBucketversioning":
			parseGetBucketversioningResp(respXML, isFormat);
			break;
		case "GetBucketwebsite":
			parseGetBucketwebsiteResp(respXML, isFormat);
			break;
		case "GetBucket":
			parseGetBucketResp(respXML, isFormat);
			break;
		case "GetBucketObjectversions":
			parseGetBucketObjectversionsResp(respXML, isFormat);
			break;
		case "InitiateMultipartUpload":
			parseInitiateMultipartUploadResp(respXML, isFormat);
			break;
		case "ListParts":
			parseListPartsResp(respXML, isFormat);
			break;
		case "ListMultipartUploads":
			parseListMultipartUploadsResp(respXML, isFormat);
			break;
		case "CompleteMultipartUpload":
			parseCompleteMultipartUploadResp(respXML, isFormat);
			break;
		default:
			System.out.println(respXML);
		}
		System.out.println("=====================================================================================");
	}

	private void parseGetServiceResp(String respXML, boolean isFormat) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputStream is = StringTOInputStream(respXML);
		Document document = db.parse(is);
		NodeList list_buckets_result = document.getChildNodes();
		int length = list_buckets_result.getLength();
		for (int i = 0; i < length; ++i) {
			Node root_node = list_buckets_result.item(i);
			if (root_node.getNodeName() != "ListAllMyBucketsResult") {
				continue;
				// return subNode.getTextContent();
			}
			System.out.println("<ListAllMyBucketsResult>");
			
			NodeList results = root_node.getChildNodes();
			int inter_length = results.getLength();
			int bucket_num = 0;
			for (int j = 0; j < inter_length; ++j) {
				Node subNode = results.item(j);
				switch (subNode.getNodeName()) {
				case "Owner": {
					System.out.println("  <Owner>");
					NodeList owner_results = subNode.getChildNodes();
					int owner_length = owner_results.getLength();
					for (int owner_index = 0; owner_index < owner_length; ++owner_index) {
						Node ownerSubNode = owner_results.item(owner_index);
						switch (ownerSubNode.getNodeName()) {
						case "ID":
							System.out.println("    <ID>" + ownerSubNode.getTextContent() + "</ID>");
							break;
						case "DisplayName":
							System.out.println("    <DisplayName>" + ownerSubNode.getTextContent() + "</DisplayName>");
							break;
						default:
							System.out.println("Unknown format!!!");
							break;
						}
					}
					System.out.println("  </Owner>");
					break;
				}
				case "Buckets": {
					System.out.println("  <Buckets>");
					NodeList buckets_results = subNode.getChildNodes();
					int buckets_length = buckets_results.getLength();
					for (int buckets_index = 0; buckets_index < buckets_length; ++buckets_index) {
						Node bucketsSubNode = buckets_results.item(buckets_index);
						switch (bucketsSubNode.getNodeName()) {
						case "Bucket": {
							System.out.println("-----------------------------<" + ++bucket_num + ">-------------------------------");
							System.out.println("    <Bucket>");
							NodeList bucket_results = bucketsSubNode.getChildNodes();
							int bucket_length = bucket_results.getLength();
							for (int bucket_index = 0; bucket_index < bucket_length; ++bucket_index) {
								Node bucketSubNode = bucket_results.item(bucket_index);
								switch (bucketSubNode.getNodeName()) {
								case "Name":
									System.out.println("      <Name>" + bucketSubNode.getTextContent() + "</Name>");
									break;
								case "CreationDate":
									System.out.println("      <CreationDate>" + bucketSubNode.getTextContent() + "</CreationDate>");
									break;
								default:
									System.out.println("Unknown format!!!");
									break;
								}
							}
							System.out.println("    </Bucket>");
							break;
						}
						default:
							System.out.println("Unknown format!!!");
							break;
						}
					}
					System.out.println("----------------------------------------------------------------");
					System.out.println("  </Buckets>");
					break;
				}
				default:
					System.out.println("  Unknown format!!!");
					break;
				}
			}
			
			System.out.println("</ListAllMyBucketsResult>");
		}
	}

	private void parseGetBucketResp(String respXML, boolean isFormat) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputStream is = StringTOInputStream(respXML);
		Document document = db.parse(is);
		NodeList list_bucket_result = document.getChildNodes();
		int length = list_bucket_result.getLength();
		for (int i = 0; i < length; ++i) {
			Node root_node = list_bucket_result.item(i);
			if (root_node.getNodeName() != "ListBucketResult") {
				continue;
				// return subNode.getTextContent();
			}
			
			System.out.println("<ListBucketResult>");
			
			NodeList results = root_node.getChildNodes();
			int inter_length = results.getLength();
			int content_num = 0;
			int comm_prefix_num = 0;
			for (int j = 0; j < inter_length; ++j) {
				Node subNode = results.item(j);
				switch (subNode.getNodeName()) {
				case "Name":
					System.out.println("  <Name>" + subNode.getTextContent() + "</Name>");
					break;
				case "Prefix":
					System.out.println("  <Prefix>" + subNode.getTextContent() + "</Prefix>");
					break;
				case "StartAfter":
					System.out.println("  <StartAfter>" + subNode.getTextContent() + "</StartAfter>");
					break;
				case "KeyCount":
					System.out.println("  <KeyCount>" + subNode.getTextContent() + "</KeyCount>");
					break;
				case "MaxKeys":
					System.out.println("  <MaxKeys>" + subNode.getTextContent() + "</MaxKeys>");
					break;
				case "IsTruncated":
					System.out.println("  <IsTruncated>" + subNode.getTextContent() + "</IsTruncated>");
					break;
				case "Delimiter":
					System.out.println("  <Delimiter>" + subNode.getTextContent() + "</Delimiter>");
					break;
				case "Encoding-Type":
					System.out.println("  <Encoding-Type>" + subNode.getTextContent() + "</Encoding-Type>");
					break;
				case "ContinuationToken":
					System.out.println("  <ContinuationToken>" + subNode.getTextContent() + "</ContinuationToken>");
					break;
				case "NextContinuationToken":
					System.out.println("  <NextContinuationToken>" + subNode.getTextContent() + "</NextContinuationToken>");
					break;
				case "Marker":
					System.out.println("  <Marker>" + subNode.getTextContent() + "</Marker>");
					break;
				case "NextMarker":
					System.out.println("  <NextMarker>" + subNode.getTextContent() + "</NextMarker>");
					break;
				case "Contents": {
					System.out.println("  -----------------------------<" + ++content_num + ">-------------------------------");
					System.out.println("  <Contents>");
					NodeList contents_results = subNode.getChildNodes();
					int contents_length = contents_results.getLength();
					for (int contents_index = 0; contents_index < contents_length; ++contents_index) {
						Node contentsSubNode = contents_results.item(contents_index);
						switch (contentsSubNode.getNodeName()) {
						case "Key":
							System.out.println("    <Key>" + contentsSubNode.getTextContent() + "</Key>");
							break;
						case "LastModified":
							System.out.println("    <LastModified>" + contentsSubNode.getTextContent() + "</LastModified>");
							break;
						case "ETag":
							System.out.println("    <ETag>" + contentsSubNode.getTextContent() + "</ETag>");
							break;
						case "Size":
							System.out.println("    <Size>" + contentsSubNode.getTextContent() + "</Size>");
							break;
						case "StorageClass":
							System.out.println("    <StorageClass>" + contentsSubNode.getTextContent() + "</StorageClass>");
							break;
						case "Owner": {
							System.out.println("    <Owner>");
							NodeList owner_results = contentsSubNode.getChildNodes();
							int owner_length = owner_results.getLength();
							for (int owner_index = 0; owner_index < owner_length; ++owner_index) {
								Node ownerSubNode = owner_results.item(owner_index);
								switch (ownerSubNode.getNodeName()) {
								case "ID":
									System.out.println("      <ID>" + ownerSubNode.getTextContent() + "</ID>");
									break;
								case "DisplayName":
									System.out.println("      <DisplayName>" + ownerSubNode.getTextContent() + "</DisplayName>");
									break;
								default:
									System.out.println("      Unknown format!!!");
									break;
								}
							}
							System.out.println("    </Owner>");
							break;
						}
						default:
							System.out.println("    Unknown format!!!");
							break;
						}
					}
					System.out.println("  </Contents>");
					break;
				}
				case "CommonPrefixes": {
					System.out.println("  -----------------------------<" + ++comm_prefix_num + ">-------------------------------");
					System.out.println("  <CommonPrefixes>");
					NodeList comm_prefix_results = subNode.getChildNodes();
					int comm_prefix_length = comm_prefix_results.getLength();
					for (int comm_prefix_index = 0; comm_prefix_index < comm_prefix_length; ++comm_prefix_index) {
						Node commPrefixSubNode = comm_prefix_results.item(comm_prefix_index);
						switch (commPrefixSubNode.getNodeName()) {
						case "Prefix":
							System.out.println("    <Prefix>" + commPrefixSubNode.getTextContent() + "</Prefix>");
							break;
						case "Owner": {
							System.out.println("    <Owner>");
							NodeList owner_results = commPrefixSubNode.getChildNodes();
							int owner_length = owner_results.getLength();
							for (int owner_index = 0; owner_index < owner_length; ++owner_index) {
								Node ownerSubNode = owner_results.item(owner_index);
								switch (ownerSubNode.getNodeName()) {
								case "ID":
									System.out.println("      <ID>" + ownerSubNode.getTextContent() + "</ID>");
									break;
								case "DisplayName":
									System.out.println("      <DisplayName>" + ownerSubNode.getTextContent() + "</DisplayName>");
									break;
								default:
									System.out.println("      Unknown format!!!");
									break;
								}
							}
							System.out.println("    </Owner>");
							break;
						}
						default:
							System.out.println("    Unknown format!!!");
							break;
						}
					}
					System.out.println("  </CommonPrefixes>");
					break;
				}
				default:
					System.out.println("  Unknown format!!!");
					break;
				}
			}
			System.out.println("  ----------------------------------------------------------------");
			System.out.println("</ListBucketResult>");
		}
	}
	
	private void parseGetBucketObjectversionsResp(String respXML, boolean isFormat) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputStream is = StringTOInputStream(respXML);
		Document document = db.parse(is);
		NodeList list_bucket_result = document.getChildNodes();
		int length = list_bucket_result.getLength();
		for (int i = 0; i < length; ++i) {
			Node root_node = list_bucket_result.item(i);
			if (root_node.getNodeName() != "ListVersionsResult") {
				continue;
				// return subNode.getTextContent();
			}
			
			System.out.println("<ListVersionsResult>");
			
			NodeList results = root_node.getChildNodes();
			int inter_length = results.getLength();
			int version_num = 0;
			int delete_marker_num = 0;
			for (int j = 0; j < inter_length; ++j) {
				Node subNode = results.item(j);
				switch (subNode.getNodeName()) {
				case "Name":
					System.out.println("  <Name>" + subNode.getTextContent() + "</Name>");
					break;
				case "Prefix":
					System.out.println("  <Prefix>" + subNode.getTextContent() + "</Prefix>");
					break;
				case "MaxKeys":
					System.out.println("  <MaxKeys>" + subNode.getTextContent() + "</MaxKeys>");
					break;
				case "IsTruncated":
					System.out.println("  <IsTruncated>" + subNode.getTextContent() + "</IsTruncated>");
					break;
				case "Delimiter":
					System.out.println("  <Delimiter>" + subNode.getTextContent() + "</Delimiter>");
					break;
				case "Encoding-Type":
					System.out.println("  <Encoding-Type>" + subNode.getTextContent() + "</Encoding-Type>");
					break;
				case "KeyMarker":
					System.out.println("  <KeyMarker>" + subNode.getTextContent() + "</KeyMarker>");
					break;
				case "NextKeyMarker":
					System.out.println("  <NextKeyMarker>" + subNode.getTextContent() + "</NextKeyMarker>");
					break;
				case "VersionIdMarker":
					System.out.println("  <VersionIdMarker>" + subNode.getTextContent() + "</VersionIdMarker>");
					break;
				case "NextVersionIdMarker":
					System.out.println("  <NextVersionIdMarker>" + subNode.getTextContent() + "</NextVersionIdMarker>");
					break;
				case "Version": {
					System.out.println("  -----------------------------<v" + ++version_num + ">-------------------------------");
					System.out.println("  <Version>");
					NodeList version_results = subNode.getChildNodes();
					int version_length = version_results.getLength();
					for (int version_index = 0; version_index < version_length; ++version_index) {
						Node versionSubNode = version_results.item(version_index);
						switch (versionSubNode.getNodeName()) {
						case "Key":
							System.out.println("    <Key>" + versionSubNode.getTextContent() + "</Key>");
							break;
						case "VersionId":
							System.out.println("    <VersionId>" + versionSubNode.getTextContent() + "</VersionId>");
							break;
						case "IsLatest":
							System.out.println("    <IsLatest>" + versionSubNode.getTextContent() + "</IsLatest>");
							break;
						case "LastModified":
							System.out.println("    <LastModified>" + versionSubNode.getTextContent() + "</LastModified>");
							break;
						case "ETag":
							System.out.println("    <ETag>" + versionSubNode.getTextContent() + "</ETag>");
							break;
						case "Size":
							System.out.println("    <Size>" + versionSubNode.getTextContent() + "</Size>");
							break;
						case "StorageClass":
							System.out.println("    <StorageClass>" + versionSubNode.getTextContent() + "</StorageClass>");
							break;
						case "Owner": {
							System.out.println("    <Owner>");
							NodeList owner_results = versionSubNode.getChildNodes();
							int owner_length = owner_results.getLength();
							for (int owner_index = 0; owner_index < owner_length; ++owner_index) {
								Node ownerSubNode = owner_results.item(owner_index);
								switch (ownerSubNode.getNodeName()) {
								case "ID":
									System.out.println("      <ID>" + ownerSubNode.getTextContent() + "</ID>");
									break;
								case "DisplayName":
									System.out.println("      <DisplayName>" + ownerSubNode.getTextContent() + "</DisplayName>");
									break;
								default:
									System.out.println("      Unknown format!!!");
									break;
								}
							}
							System.out.println("    </Owner>");
							break;
						}
						default:
							System.out.println("    Unknown format!!!");
							break;
						}
					}
					System.out.println("  </Version>");
					break;
				}
				case "DeleteMarker": {
					System.out.println("  -----------------------------<d" + ++delete_marker_num + ">-------------------------------");
					System.out.println("  <DeleteMarker>");
					NodeList delete_marker_results = subNode.getChildNodes();
					int delete_marker_length = delete_marker_results.getLength();
					for (int delete_marker_index = 0; delete_marker_index < delete_marker_length; ++delete_marker_index) {
						Node deleteMarkerSubNode = delete_marker_results.item(delete_marker_index);
						switch (deleteMarkerSubNode.getNodeName()) {
						case "Key":
							System.out.println("    <Key>" + deleteMarkerSubNode.getTextContent() + "</Key>");
							break;
						case "VersionId":
							System.out.println("    <VersionId>" + deleteMarkerSubNode.getTextContent() + "</VersionId>");
							break;
						case "IsLatest":
							System.out.println("    <IsLatest>" + deleteMarkerSubNode.getTextContent() + "</IsLatest>");
							break;
						case "LastModified":
							System.out.println("    <LastModified>" + deleteMarkerSubNode.getTextContent() + "</LastModified>");
							break;
						case "Owner": {
							System.out.println("    <Owner>");
							NodeList owner_results = deleteMarkerSubNode.getChildNodes();
							int owner_length = owner_results.getLength();
							for (int owner_index = 0; owner_index < owner_length; ++owner_index) {
								Node ownerSubNode = owner_results.item(owner_index);
								switch (ownerSubNode.getNodeName()) {
								case "ID":
									System.out.println("      <ID>" + ownerSubNode.getTextContent() + "</ID>");
									break;
								case "DisplayName":
									System.out.println("      <DisplayName>" + ownerSubNode.getTextContent() + "</DisplayName>");
									break;
								default:
									System.out.println("      Unknown format!!!");
									break;
								}
							}
							System.out.println("    </Owner>");
							break;
						}
						default:
							System.out.println("    Unknown format!!!");
							break;
						}
					}
					System.out.println("  </DeleteMarker>");
					break;
				}
				default:
					System.out.println("  Unknown format!!!");
					break;
				}
			}
			System.out.println("  ----------------------------------------------------------------");
			System.out.println("</ListVersionsResult>");
		}
	}
	
	private void parsePutObjectCopyResp(String respXML, boolean isFormat) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputStream is = StringTOInputStream(respXML);
		Document document = db.parse(is);
		NodeList comp_multi_upload_result = document.getChildNodes();
		int length = comp_multi_upload_result.getLength();
		for (int i = 0; i < length; ++i) {
			Node node = comp_multi_upload_result.item(i);
			if (node.getNodeName() != "CopyObjectResult") {
				continue;
				// return subNode.getTextContent();
			}
			
			System.out.println("<CopyObjectResult>");
			
			NodeList results = node.getChildNodes();
			int inter_length = results.getLength();
			for (int j = 0; j < inter_length; ++j) {
				Node subNode = results.item(j);
				switch (subNode.getNodeName()) {
				case "LastModified":
					System.out.println("  <LastModified>" + subNode.getTextContent() + "</LastModified>");
					break;
				case "ETag":
					System.out.println("  <ETag>" + subNode.getTextContent() + "</ETag>");
					break;
				default:
					System.out.println("  Unknown format!!!");
					break;
				}
			}
			
			System.out.println("</CopyObjectResult>");
		}
	}
	
	private void parseGetBucketAndObjectaclResp(String respXML, boolean isFormat) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputStream is = StringTOInputStream(respXML);
		Document document = db.parse(is);
		NodeList acp_result = document.getChildNodes();
		int length = acp_result.getLength();
		for (int i = 0; i < length; ++i) {
			Node root_node = acp_result.item(i);
			if (root_node.getNodeName() != "AccessControlPolicy") {
				continue;
				// return subNode.getTextContent();
			}
			
			System.out.println("<AccessControlPolicy>");
			
			NodeList results = root_node.getChildNodes();
			int inter_length = results.getLength();
			int grant_num = 0;
			for (int j = 0; j < inter_length; ++j) {
				Node subNode = results.item(j);
				switch (subNode.getNodeName()) {
				case "Owner": {
					System.out.println("  <Owner>");
					NodeList owner_results = subNode.getChildNodes();
					int owner_length = owner_results.getLength();
					for (int owner_index = 0; owner_index < owner_length; ++owner_index) {
						Node ownerSubNode = owner_results.item(owner_index);
						switch (ownerSubNode.getNodeName()) {
						case "ID":
							System.out.println("    <ID>" + ownerSubNode.getTextContent() + "</ID>");
							break;
						case "DisplayName":
							System.out.println("    <DisplayName>" + ownerSubNode.getTextContent() + "</DisplayName>");
							break;
						default:
							System.out.println("    Unknown format!!!");
							break;
						}
					}
					System.out.println("  </Owner>");
					break;
				}
				case "AccessControlList": {
					System.out.println("  <AccessControlList>");
					NodeList acl_results = subNode.getChildNodes();
					int acl_length = acl_results.getLength();
					for (int acl_index = 0; acl_index < acl_length; ++acl_index) {
						Node aclSubNode = acl_results.item(acl_index);
						switch (aclSubNode.getNodeName()) {
						case "Grant": {
							System.out.println("    -----------------------------<" + ++grant_num + ">-------------------------------");
							System.out.println("    <Grant>");
							NodeList grant_results = aclSubNode.getChildNodes();
							int grant_length = grant_results.getLength();
							for (int grant_index = 0; grant_index < grant_length; ++grant_index) {
								Node grantSubNode = grant_results.item(grant_index);
								switch (grantSubNode.getNodeName()) {
								case "Grantee": {
									System.out.println("      <Grantee>");
									NodeList grantee_results = grantSubNode.getChildNodes();
									int grantee_length = grantee_results.getLength();
									for (int grantee_index = 0; grantee_index < grantee_length; ++grantee_index) {
										Node granteeSubNode = grantee_results.item(grantee_index);
										switch (granteeSubNode.getNodeName()) {
										case "ID": 
											System.out.println("        <ID>" + granteeSubNode.getTextContent() + "</ID>");											
											break;
										case "DisplayName":
											System.out.println("        <DisplayName>" + granteeSubNode.getTextContent() + "</DisplayName>");											
											break;
										case "URI":
											System.out.println("        <URI>" + granteeSubNode.getTextContent() + "</URI>");											
											break;
										default:
											System.out.println("Unknown format!!!");
											break;
										}
									}
									System.out.println("      </Grantee>");
									break;
								}
								case "Permission":
									System.out.println("      <Permission>" + grantSubNode.getTextContent() + "</Permission>");
									break;
								default:
									System.out.println("      Unknown format!!!");
									break;
								}
							}
							System.out.println("    </Grant>");
							break;
						}
						default:
							System.out.println("    Unknown format!!!");
							break;
						}
					}
					System.out.println("    ----------------------------------------------------------------");
					System.out.println("  </AccessControlList>");
					break;
				}
				default:
					System.out.println("  Unknown format!!!");
					break;
				}
			}
			
			System.out.println("</AccessControlPolicy>");
		}
	}
	
	private void parseDeleteMultipleObjectsResp(String respXML, boolean isFormat) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputStream is = StringTOInputStream(respXML);
		Document document = db.parse(is);
		NodeList list_parts_result = document.getChildNodes();
		int length = list_parts_result.getLength();
		for (int i = 0; i < length; ++i) {
			Node root_node = list_parts_result.item(i);
			if (root_node.getNodeName() != "DeleteResult") {
				continue;
				// return subNode.getTextContent();
			}
			
			System.out.println("<DeleteResult>");
			
			NodeList results = root_node.getChildNodes();
			int inter_length = results.getLength();
			int deleted_num = 0;
			int error_num = 0;
			for (int j = 0; j < inter_length; ++j) {
				Node subNode = results.item(j);
				switch (subNode.getNodeName()) {
				case "Deleted": {
					System.out.println("  -----------------------------<" + ++deleted_num + ">-------------------------------");
					System.out.println("  <Deleted>");
					NodeList deleted_results = subNode.getChildNodes();
					int deleted_length = deleted_results.getLength();
					for (int deleted_index = 0; deleted_index < deleted_length; ++deleted_index) {
						Node deletedSubNode = deleted_results.item(deleted_index);
						switch (deletedSubNode.getNodeName()) {
						case "Key":
							System.out.println("    <Key>" + deletedSubNode.getTextContent() + "</Key>");
							break;
						case "VersionId":
							System.out.println("    <VersionId>" + deletedSubNode.getTextContent() + "</VersionId>");
							break;
						case "DeleteMarker":
							System.out.println("    <DeleteMarker>" + deletedSubNode.getTextContent() + "</DeleteMarker>");
							break;
						case "DeleteMarkerVersionId":
							System.out.println("    <DeleteMarkerVersionId>" + deletedSubNode.getTextContent() + "</DeleteMarkerVersionId>");
							break;
						default:
							System.out.println("    Unknown format!!!");
							break;
						}
					}
					System.out.println("  </Deleted>");
					break;
				}
				case "Error": {
					System.out.println("  -----------------------------<" + ++error_num + ">-------------------------------");
					System.out.println("  <Error>");
					NodeList error_results = subNode.getChildNodes();
					int error_length = error_results.getLength();
					for (int error_index = 0; error_index < error_length; ++error_index) {
						Node errorSubNode = error_results.item(error_index);
						switch (errorSubNode.getNodeName()) {
						case "Key":
							System.out.println("    <Key>" + errorSubNode.getTextContent() + "</Key>");
							break;
						case "VersionId":
							System.out.println("    <VersionId>" + errorSubNode.getTextContent() + "</VersionId>");
							break;
						case "Code":
							System.out.println("    <Code>" + errorSubNode.getTextContent() + "</Code>");
							break;
						case "Message":
							System.out.println("    <Message>" + errorSubNode.getTextContent() + "</Message>");
							break;
						default:
							System.out.println("    Unknown format!!!");
							break;
						}
					}
					System.out.println("  </Error>");
					break;
				}
				default:
					System.out.println("  Unknown format!!!");
					break;
				}
			}
			
			System.out.println("  ----------------------------------------------------------------");
			System.out.println("</DeleteResult>");
		}
	}
	
	private void parseGetBucketversioningResp(String respXML, boolean isFormat)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputStream is = StringTOInputStream(respXML);
		Document document = db.parse(is);
		NodeList upload_result = document.getChildNodes();
		int length = upload_result.getLength();
		for (int i = 0; i < length; ++i) {
			Node node = upload_result.item(i);
			if (node.getNodeName() != "VersioningConfiguration") {
				continue;
				// return subNode.getTextContent();
			}
			
			System.out.println("<VersioningConfiguration>");
			
			NodeList results = node.getChildNodes();
			int inter_length = results.getLength();
			for (int j = 0; j < inter_length; ++j) {
				Node subNode = results.item(j);
				switch (subNode.getNodeName()) {
				case "Status":
					System.out.println("  <Status>" + subNode.getTextContent() + "</Status>");
					break;
				default:
					System.out.println("  Unknown format!!!");
					break;
				}
			}
			
			System.out.println("</VersioningConfiguration>");
		}
	}
	
	private void parseGetBucketlifecycleResp(String respXML, boolean isFormat) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputStream is = StringTOInputStream(respXML);
		Document document = db.parse(is);
		NodeList list_parts_result = document.getChildNodes();
		int length = list_parts_result.getLength();
		for (int i = 0; i < length; ++i) {
			Node root_node = list_parts_result.item(i);
			if (root_node.getNodeName() != "LifecycleConfiguration") {
				continue;
			}
			
			System.out.println("<LifecycleConfiguration>");
			
			NodeList results = root_node.getChildNodes();
			int inter_length = results.getLength();
			int rule_num = 0;
			for (int j = 0; j < inter_length; ++j) {
				Node subNode = results.item(j);
				switch (subNode.getNodeName()) {
				case "Rule": {
					System.out.println("  -----------------------------<" + ++rule_num + ">-------------------------------");
					System.out.println("  <Rule>");
					NodeList rule_results = subNode.getChildNodes();
					int rule_length = rule_results.getLength();
					for (int rule_index = 0; rule_index < rule_length; ++rule_index) {
						Node ruleSubNode = rule_results.item(rule_index);
						switch (ruleSubNode.getNodeName()) {
						case "ID":
							System.out.println("    <ID>" + ruleSubNode.getTextContent() + "</ID>");
							break;
						case "Prefix":
							System.out.println("    <Prefix>" + ruleSubNode.getTextContent() + "</Prefix>");
							break;
						case "Status":
							System.out.println("    <Status>" + ruleSubNode.getTextContent() + "</Status>");
							break;
						case "Transition": {
							System.out.println("    <Transition>");
							NodeList transition_results = ruleSubNode.getChildNodes();
							int transition_length = transition_results.getLength();
							for (int transition_index = 0; transition_index < transition_length; ++transition_index) {
								Node transitionSubNode = transition_results.item(transition_index);
								switch (transitionSubNode.getNodeName()) {
								case "Days":
									System.out.println("      <Days>" + transitionSubNode.getTextContent() + "</Days>");
									break;
								case "StartAt":
									System.out.println("      <StartAt>" + transitionSubNode.getTextContent() + "</StartAt>");
									break;
								case "StopAt":
									System.out.println("      <Type>" + transitionSubNode.getTextContent() + "</Type>");
									break;
								case "Type":
									System.out.println("      <Type>" + transitionSubNode.getTextContent() + "</Type>");
									break;
								case "StorageClass":
									System.out.println("      <StorageClass>" + transitionSubNode.getTextContent() + "</StorageClass>");
									break;
								default:
									System.out.println("      Unknown format!!!");
									break;
								}
							}
							System.out.println("    </Transition>");
							break;
						}
						case "Expiration": {
							System.out.println("    <Expiration>");
							NodeList expiration_results = ruleSubNode.getChildNodes();
							int expiration_length = expiration_results.getLength();
							for (int expiration_index = 0; expiration_index < expiration_length; ++expiration_index) {
								Node expirationSubNode = expiration_results.item(expiration_index);
								switch (expirationSubNode.getNodeName()) {
								case "Days":
									System.out.println("      <Days>" + expirationSubNode.getTextContent() + "</Days>");
									break;
								case "StartAt":
									System.out.println("      <StartAt>" + expirationSubNode.getTextContent() + "</StartAt>");
									break;
								case "StopAt":
									System.out.println("      <Type>" + expirationSubNode.getTextContent() + "</Type>");
									break;
								default:
									System.out.println("      Unknown format!!!");
									break;
								}
							}
							System.out.println("    </Expiration>");
							break;
						}
						default:
							System.out.println("    Unknown format!!!");
							break;
						}
					}
					System.out.println("  </Rule>");
					break;
				}
				default:
					System.out.println("  Unknown format!!!");
					break;
				}
			}
			
			System.out.println("  ----------------------------------------------------------------");
			System.out.println("</LifecycleConfiguration>");
		}
	}
	
	private void parseGetBucketwebsiteResp(String respXML, boolean isFormat) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputStream is = StringTOInputStream(respXML);
		Document document = db.parse(is);
		NodeList list_parts_result = document.getChildNodes();
		int length = list_parts_result.getLength();
		for (int i = 0; i < length; ++i) {
			Node root_node = list_parts_result.item(i);
			if (root_node.getNodeName() != "WebsiteConfiguration") {
				continue;
				// return subNode.getTextContent();
			}
			
			System.out.println("<WebsiteConfiguration>");
			
			NodeList results = root_node.getChildNodes();
			int inter_length = results.getLength();
			int rule_num = 0;
			for (int j = 0; j < inter_length; ++j) {
				Node subNode = results.item(j);
				switch (subNode.getNodeName()) {
				case "RedirectAllRequestsTo": {
					System.out.println("  <RedirectAllRequestsTo>");
					NodeList index_results = subNode.getChildNodes();
					int index_length = index_results.getLength();
					for (int index_index = 0; index_index < index_length; ++index_index) {
						Node indexSubNode = index_results.item(index_index);
						switch (indexSubNode.getNodeName()) {
						case "HostName":
							System.out.println("    <HostName>" + indexSubNode.getTextContent() + "</HostName>");
							break;
						case "Protocol":
							System.out.println("    <Protocol>" + indexSubNode.getTextContent() + "</Protocol>");
							break;
						default:
							System.out.println("    Unknown format!!!");
							break;
						}
					}
					System.out.println("  </RedirectAllRequestsTo>");
					break;
				}
				case "IndexDocument": {
					System.out.println("  <IndexDocument>");
					NodeList index_results = subNode.getChildNodes();
					int index_length = index_results.getLength();
					for (int index_index = 0; index_index < index_length; ++index_index) {
						Node indexSubNode = index_results.item(index_index);
						switch (indexSubNode.getNodeName()) {
						case "Suffix":
							System.out.println("    <Suffix>" + indexSubNode.getTextContent() + "</Suffix>");
							break;
						default:
							System.out.println("    Unknown format!!!");
							break;
						}
					}
					System.out.println("  </IndexDocument>");
					break;
				}
				case "ErrorDocument": {
					System.out.println("  <ErrorDocument>");
					NodeList index_results = subNode.getChildNodes();
					int index_length = index_results.getLength();
					for (int index_index = 0; index_index < index_length; ++index_index) {
						Node indexSubNode = index_results.item(index_index);
						switch (indexSubNode.getNodeName()) {
						case "Key":
							System.out.println("    <Key>" + indexSubNode.getTextContent() + "</Key>");
							break;
						default:
							System.out.println("    Unknown format!!!");
							break;
						}
					}
					System.out.println("  </ErrorDocument>");
					break;
				}
				case "RoutingRules": {
					System.out.println("  <RoutingRules>");
					NodeList rules_results = subNode.getChildNodes();
					int rules_length = rules_results.getLength();
					for (int rules_index = 0; rules_index < rules_length; ++rules_index) {
						Node rulesSubNode = rules_results.item(rules_index);
						switch (rulesSubNode.getNodeName()) {
						case "RoutingRule": {
							System.out.println("    -----------------------------<" + ++rule_num + ">-------------------------------");
							System.out.println("    <RoutingRule>");
							NodeList rule_results = rulesSubNode.getChildNodes();
							int rule_length = rule_results.getLength();
							for (int rule_index = 0; rule_index < rule_length; ++rule_index) {
								Node ruleSubNode = rule_results.item(rule_index);
								switch (ruleSubNode.getNodeName()) {
								case "Condition": {
									System.out.println("      <Condition>");
									NodeList condition_results = ruleSubNode.getChildNodes();
									int condition_length = condition_results.getLength();
									for (int condition_index = 0; condition_index < condition_length; ++condition_index) {
										Node conditionSubNode = condition_results.item(condition_index);
										switch (conditionSubNode.getNodeName()) {
										case "KeyPrefixEquals":
											System.out.println("        <KeyPrefixEquals>" + conditionSubNode.getTextContent() + "</KeyPrefixEquals>");
											break;
										case "HttpErrorCodeReturnedEquals":
											System.out.println("        <HttpErrorCodeReturnedEquals>" + conditionSubNode.getTextContent() + "</HttpErrorCodeReturnedEquals>");
											break;
										default:
											System.out.println("        Unknown format!!!");
											break;
										}
									}
									System.out.println("      </Condition>");
									break;
								}
								case "Redirect": {
									System.out.println("      <Redirect>");
									NodeList redirect_results = ruleSubNode.getChildNodes();
									int redirect_length = redirect_results.getLength();
									for (int redirect_index = 0; redirect_index < redirect_length; ++redirect_index) {
										Node redirectSubNode = redirect_results.item(redirect_index);
										switch (redirectSubNode.getNodeName()) {
										case "Protocol":
											System.out.println("        <Protocol>" + redirectSubNode.getTextContent() + "</Protocol>");
											break;
										case "HostName":
											System.out.println("        <HostName>" + redirectSubNode.getTextContent() + "</HostName>");
											break;
										case "ReplaceKeyPrefixWith":
											System.out.println("        <ReplaceKeyPrefixWith>" + redirectSubNode.getTextContent() + "</ReplaceKeyPrefixWith>");
											break;
										case "ReplaceKeyWith":
											System.out.println("        <ReplaceKeyWith>" + redirectSubNode.getTextContent() + "</ReplaceKeyWith>");
											break;
										case "HttpRedirectCode":
											System.out.println("        <HttpRedirectCode>" + redirectSubNode.getTextContent() + "</HttpRedirectCode>");
											break;
										default:
											System.out.println("        Unknown format!!!");
											break;
										}
									}
									System.out.println("      </Redirect>");
									break;
								}
								default:
									System.out.println("      Unknown format!!!");
									break;
								}
							}
							System.out.println("    </RoutingRule>");
							break;
						}
						default:
							System.out.println("    Unknown format!!!");
							break;
						}
					}
					System.out.println("    ----------------------------------------------------------------");
					System.out.println("  </RoutingRules>");
					break;
				}
				default:
					System.out.println("  Unknown format!!!");
					break;
				}
			}
			
			System.out.println("</WebsiteConfiguration>");
		}
	}
	
	private void parseInitiateMultipartUploadResp(String respXML, boolean isFormat)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputStream is = StringTOInputStream(respXML);
		Document document = db.parse(is);
		NodeList upload_result = document.getChildNodes();
		int length = upload_result.getLength();
		for (int i = 0; i < length; ++i) {
			Node node = upload_result.item(i);
			if (node.getNodeName() != "InitiateMultipartUploadResult") {
				continue;
				// return subNode.getTextContent();
			}
			
			System.out.println("<InitiateMultipartUploadResult>");
			
			NodeList results = node.getChildNodes();
			int inter_length = results.getLength();
			for (int j = 0; j < inter_length; ++j) {
				Node subNode = results.item(j);
				switch (subNode.getNodeName()) {
				case "Bucket":
					System.out.println("  <Bucket>" + subNode.getTextContent() + "</Bucket>");
					break;
				case "Key":
					System.out.println("  <Key>" + subNode.getTextContent() + "</Key>");
					break;
				case "UploadId":
					System.out.println("  <UploadId>" + subNode.getTextContent() + "</UploadId>");
					break;
				default:
					System.out.println("  Unknown format!!!");
					break;
				}
			}
			
			System.out.println("</InitiateMultipartUploadResult>");
		}
	}
	
	private void parseListPartsResp(String respXML, boolean isFormat) throws ParserConfigurationException, SAXException, IOException {
		if (isFormat) {
			parseListPartsRespFmt(respXML);
			return;
		}
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputStream is = StringTOInputStream(respXML);
		Document document = db.parse(is);
		NodeList list_parts_result = document.getChildNodes();
		int length = list_parts_result.getLength();
		for (int i = 0; i < length; ++i) {
			Node root_node = list_parts_result.item(i);
			if (root_node.getNodeName() != "ListPartsResult") {
				continue;
				// return subNode.getTextContent();
			}
			
			System.out.println("<ListPartsResult>");
			
			NodeList results = root_node.getChildNodes();
			int inter_length = results.getLength();
			int part_num = 0;
			for (int j = 0; j < inter_length; ++j) {
				Node subNode = results.item(j);
				switch (subNode.getNodeName()) {
				case "Bucket":
					System.out.println("  <Bucket>" + subNode.getTextContent() + "</Bucket>");
					break;
				case "Key":
					System.out.println("  <Key>" + subNode.getTextContent() + "</Key>");
					break;
				case "UploadId":
					System.out.println("  <UploadId>" + subNode.getTextContent() + "</UploadId>");
					break;
				case "StorageClass":
					System.out.println("  <StorageClass>" + subNode.getTextContent() + "</StorageClass>");
					break;
				case "PartNumberMarker":
					System.out.println("  <PartNumberMarker>" + subNode.getTextContent() + "</PartNumberMarker>");
					break;
				case "NextPartNumberMarker":
					System.out.println("  <NextPartNumberMarker>" + subNode.getTextContent() + "</NextPartNumberMarker>");
					break;
				case "MaxParts":
					System.out.println("  <MaxParts>" + subNode.getTextContent() + "</MaxParts>");
					break;
				case "IsTruncated":
					System.out.println("  <IsTruncated>" + subNode.getTextContent() + "</IsTruncated>");
					break;
				case "Owner": {
					System.out.println("  <Owner>");
					NodeList owner_results = subNode.getChildNodes();
					int owner_length = owner_results.getLength();
					for (int owner_index = 0; owner_index < owner_length; ++owner_index) {
						Node ownerSubNode = owner_results.item(owner_index);
						switch (ownerSubNode.getNodeName()) {
						case "ID":
							System.out.println("    <ID>" + ownerSubNode.getTextContent() + "</ID>");
							break;
						case "DisplayName":
							System.out.println("    <DisplayName>" + ownerSubNode.getTextContent() + "</DisplayName>");
							break;
						default:
							System.out.println("    Unknown format!!!");
							break;
						}
					}
					System.out.println("  </Owner>");
					break;
				}
				case "Part": {
					System.out.println("-----------------------------<" + ++part_num + ">-------------------------------");
					System.out.println("  <Part>");
					NodeList part_results = subNode.getChildNodes();
					int part_length = part_results.getLength();
					for (int part_index = 0; part_index < part_length; ++part_index) {
						Node partSubNode = part_results.item(part_index);
						switch (partSubNode.getNodeName()) {
						case "LastModified":
							System.out.println("    <LastModified>" + partSubNode.getTextContent() + "</LastModified>");
							break;
						case "PartNumber":
							System.out.println("    <PartNumber>" + partSubNode.getTextContent() + "</PartNumber>");
							break;
						case "ETag":
							System.out.println("    <ETag>" + partSubNode.getTextContent() + "</ETag>");
							break;
						case "Size":
							System.out.println("    <Size>" + partSubNode.getTextContent() + "</Size>");
							break;
						default:
							System.out.println("    Unknown format!!!");
							break;
						}
					}
					System.out.println("  </Part>");
					break;
				}
				default:
					System.out.println("  Unknown format!!!");
					break;
				}
			}
			
			System.out.println("</ListPartsResult>");
		}
	}
	
	private void parseListPartsRespFmt(String respXML) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputStream is = StringTOInputStream(respXML);
		Document document = db.parse(is);
		NodeList list_parts_result = document.getChildNodes();
		int length = list_parts_result.getLength();
		for (int i = 0; i < length; ++i) {
			Node root_node = list_parts_result.item(i);
			if (root_node.getNodeName() != "ListPartsResult") {
				continue;
			}
			
			System.out.println("<CompleteMultipartUpload>");
			
			NodeList results = root_node.getChildNodes();
			int inter_length = results.getLength();
			for (int j = 0; j < inter_length; ++j) {
				Node subNode = results.item(j);
				switch (subNode.getNodeName()) {
				case "Part": {
					System.out.println("  <Part>");
					NodeList part_results = subNode.getChildNodes();
					int part_length = part_results.getLength();
					for (int part_index = 0; part_index < part_length; ++part_index) {
						Node partSubNode = part_results.item(part_index);
						switch (partSubNode.getNodeName()) {
						case "PartNumber":
							System.out.println("    <PartNumber>" + partSubNode.getTextContent() + "</PartNumber>");
							break;
						case "ETag":
							System.out.println("    <ETag>" + partSubNode.getTextContent() + "</ETag>");
							break;
						default:
							break;
						}
					}
					System.out.println("  </Part>");
					break;
				}
				default:
					break;
				}
			}
			
			System.out.println("</CompleteMultipartUpload>");
		}
	}

	private void parseListMultipartUploadsResp(String respXML, boolean isFormat)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputStream is = StringTOInputStream(respXML);
		Document document = db.parse(is);
		NodeList list_parts_result = document.getChildNodes();
		int length = list_parts_result.getLength();
		for (int i = 0; i < length; ++i) {
			Node root_node = list_parts_result.item(i);
			if (root_node.getNodeName() != "ListMultipartUploadsResult") {
				continue;
				// return subNode.getTextContent();
			}
			
			System.out.println("<ListMultipartUploadsResult>");
			
			NodeList results = root_node.getChildNodes();
			int inter_length = results.getLength();
			int upload_num = 0;
			for (int j = 0; j < inter_length; ++j) {
				Node subNode = results.item(j);
				switch (subNode.getNodeName()) {
				case "Bucket":
					System.out.println("  <Bucket>" + subNode.getTextContent() + "</Bucket>");
					break;
				case "NextKeyMarker":
					System.out.println("  <NextKeyMarker>" + subNode.getTextContent() + "</NextKeyMarker>");
					break;
				case "NextUploadIdMarker":
					System.out.println("  <NextUploadIdMarker>" + subNode.getTextContent() + "</NextUploadIdMarker>");
					break;
				case "MaxUploads":
					System.out.println("  <MaxUploads>" + subNode.getTextContent() + "</MaxUploads>");
					break;
				case "IsTruncated":
					System.out.println("  <IsTruncated>" + subNode.getTextContent() + "</IsTruncated>");
					break;
				case "Upload": {
					System.out.println("-----------------------------<" + ++upload_num + ">-------------------------------");
					System.out.println("  <Upload>");
					NodeList upload_results = subNode.getChildNodes();
					int upload_length = upload_results.getLength();
					for (int upload_index = 0; upload_index < upload_length; ++upload_index) {
						Node uploadSubNode = upload_results.item(upload_index);
						switch (uploadSubNode.getNodeName()) {
						case "Key":
							System.out.println("    <Key>" + uploadSubNode.getTextContent() + "</Key>");
							break;
						case "UploadId":
							System.out.println("    <UploadId>" + uploadSubNode.getTextContent() + "</UploadId>");
							break;
						case "Initiator": {
							System.out.println("    <Initiator>");
							NodeList initiator_results = uploadSubNode.getChildNodes();
							int initiator_length = initiator_results.getLength();
							for (int initiator_index = 0; initiator_index < initiator_length; ++initiator_index) {
								Node initiatorSubNode = initiator_results.item(initiator_index);
								switch (initiatorSubNode.getNodeName()) {
								case "ID":
									System.out.println("      <ID>" + initiatorSubNode.getTextContent() + "</ID>");
									break;
								case "DisplayName":
									System.out.println("      <DisplayName>" + initiatorSubNode.getTextContent() + "</DisplayName>");
									break;
								default:
									System.out.println("      Unknown format!!!");
									break;
								}
							}
							System.out.println("    </Initiator>");
							break;
						}
						case "Owner": {
							System.out.println("    <Owner>");
							NodeList owner_results = uploadSubNode.getChildNodes();
							int owner_length = owner_results.getLength();
							for (int owner_index = 0; owner_index < owner_length; ++owner_index) {
								Node ownerSubNode = owner_results.item(owner_index);
								switch (ownerSubNode.getNodeName()) {
								case "ID":
									System.out.println("      <ID>" + ownerSubNode.getTextContent() + "</ID>");
									break;
								case "DisplayName":
									System.out.println("      <DisplayName>" + ownerSubNode.getTextContent() + "</DisplayName>");
									break;
								default:
									System.out.println("      Unknown format!!!");
									break;
								}
							}
							System.out.println("    </Owner>");
							break;
						}
						case "StorageClass":
							System.out.println("    <StorageClass>" + uploadSubNode.getTextContent() + "</StorageClass>");
							break;
						case "Initiated":
							System.out.println("    <Initiated>" + uploadSubNode.getTextContent() + "</Initiated>");
							break;
						default:
							System.out.println("    Unknown format!!!");
							break;
						}
					}
					System.out.println("  </Upload>");
					break;
				}
				default:
					System.out.println("  Unknown format!!!");
					break;
				}
			}
			
			System.out.println("</ListMultipartUploadsResult>");
		}
	}

	private void parseCompleteMultipartUploadResp(String respXML, boolean isFormat)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		InputStream is = StringTOInputStream(respXML);
		Document document = db.parse(is);
		NodeList comp_multi_upload_result = document.getChildNodes();
		int length = comp_multi_upload_result.getLength();
		for (int i = 0; i < length; ++i) {
			Node node = comp_multi_upload_result.item(i);
			if (node.getNodeName() != "CompleteMultipartUploadResult") {
				continue;
				// return subNode.getTextContent();
			}
			
			System.out.println("<CompleteMultipartUploadResult>");
			
			NodeList results = node.getChildNodes();
			int inter_length = results.getLength();
			for (int j = 0; j < inter_length; ++j) {
				Node subNode = results.item(j);
				switch (subNode.getNodeName()) {
				case "Location":
					System.out.println("  <Location>" + subNode.getTextContent() + "</Location>");
					break;
				case "Bucket":
					System.out.println("  <Bucket>" + subNode.getTextContent() + "</Bucket>");
					break;
				case "Key":
					System.out.println("  <Key>" + subNode.getTextContent() + "</Key>");
					break;
				case "ETag":
					System.out.println("  <ETag>" + subNode.getTextContent() + "</ETag>");
					break;
				default:
					System.out.println("  Unknown format!!!");
					break;
				}
			}
			
			System.out.println("</CompleteMultipartUploadResult>");
		}
	}
	private InputStream StringTOInputStream(String in) throws UnsupportedEncodingException {
		ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes("ISO-8859-1"));
		return is;
	}
}
