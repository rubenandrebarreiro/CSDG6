package fct.unl.pt.csd.Ledger.Structures;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
//import java.util.zip.Adler32;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * MerkleTree is an implementation of a Merkle binary hash tree where the leaves
 * are signatures (hashes, digests, CRCs, etc.) of some underlying data structure
 * that is not explicitly part of the tree.
 * 
 * The internal leaves of the tree are signatures of its two child nodes. If an
 * internal node has only one child, the the signature of the child node is
 * adopted ("promoted").
 * 
 * MerkleTree knows how to serialize itself to a binary format, but does not
 * implement the Java Serializer interface.  The {@link #serialize()} method
 * returns a byte array, which should be passed to 
 * {@link MerkleDeserializer#deserialize(byte[])} in order to hydrate into
 * a MerkleTree in memory.
 * 
 * This MerkleTree is intentionally ignorant of the hashing/checksum algorithm
 * used to generate the leaf signatures. It uses Adler32 CRC to generate
 * signatures for all internal node signatures (other than those "promoted"
 * that have only one child).
 * 
 * The Adler32 CRC is not cryptographically secure, so this implementation
 * should NOT be used in scenarios where the data is being received from
 * an untrusted source.
 * 
 * Adapted from:
 * - https://github.com/quux00/merkle-tree
 * 
 */
public class MerkleTree {

	public static final int MAGIC_HDR = 0xcdaace99;
	
	public static final int INT_BYTES = 4;
	
	public static final int LONG_BYTES = 8;
	
	public static final byte LEAF_SIG_TYPE = 0x0;
	
	public static final byte INTERNAL_SIG_TYPE = 0x01;
  
	//private final Adler32 crc = new Adler32();
	
	private List<String> leafsSignatures;
  
	private Node treeRoot;
  
	private int treeDepth;
  
	private int numNodes;
  
	
	/**
	 * Use this constructor to create a MerkleTree from a list of Leafs' Signatures.
	 * 
	 * NOTE:
	 * - The Merkle tree is built from the bottom up.
	 * 
	 * @param leafSignatures the Leafs' Signatures
	 * @throws NoSuchPaddingException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 */
	public MerkleTree(List<String> leafsSignatures) throws InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
	
		this.constructTree(leafsSignatures);
	
	}
	
	/**
	 * Use this constructor when you have already constructed the tree of Nodes 
	 * (from deserialization).
	 * 
	 * @param treeRoot the Root of the Merkle Tree
	 * @param numNodes the Number of Nodes of the Merkle Tree
	 * @param height the height of the Merkle Tree
	 * @param leafsSignatures the List of Leafs' Signatures
	 */
	public MerkleTree(Node treeRoot, int numNodes, int height, List<String> leafsSignatures) {
	
		this.treeRoot = treeRoot;
		this.numNodes = numNodes;
		this.treeDepth = height;
		this.leafsSignatures = leafsSignatures;
	
	}
  
  
   /**
    * Performs the Serialisation of the Merkle Tree.
    * 
   	* Serialisation format:
   	* - (magicheader:int)(numnodes:int)[(nodetype:byte)(siglength:int)(signature:[]byte)]
   	*
   	* @return the Serialisation of the Merkle Tree
   	*/
	public byte[] serialize() {
	  
		int magicHeaderSize = INT_BYTES;
		int numNodeSize = INT_BYTES;
		int headerSize = ( magicHeaderSize + numNodeSize );

	    int typeByteSize = 1;
	    int signatureLength = INT_BYTES;
    
	    int parentSignaturesSize = LONG_BYTES;
	    int leafsSignaturesSize = this.leafsSignatures.get(0).getBytes(StandardCharsets.UTF_8).length;

	    // some of the internal nodes may use leaf signatures (when "promoted")
	    // so ensure that the ByteBuffer overestimates how much space is needed
	    // since ByteBuffer does not expand on demand
	    int maxSignatureSize = leafsSignaturesSize;
	    
	    if (parentSignaturesSize > maxSignatureSize) {
	    
	    	maxSignatureSize = parentSignaturesSize;
	    
	    }
        
	    int spaceForNodes = ( typeByteSize + signatureLength + maxSignatureSize ) * numNodeSize; 
	    
	    int cap = ( headerSize + spaceForNodes );
	    ByteBuffer byteBuffer = ByteBuffer.allocate(cap);
	    
	    byteBuffer.putInt(MAGIC_HDR).putInt(this.numNodes);  // header
	    this.serializeBreadthFirst(byteBuffer);
	
	    // the ByteBuf allocated space is likely more than was needed
	    // so copy to a byte array of the exact size necessary
	    byte[] serializedTree = new byte[byteBuffer.position()];
	    
	    byteBuffer.rewind();
	    byteBuffer.get(serializedTree);
    

	    return serializedTree;
	  
	}
  

   /**
	* Performs the Serialisation of the Merkle Tree, doing a Breath-First Search.
	* 
	* Serialisation format after the Header section:
	* - [(nodetype:byte)(siglength:int)(signature:[]byte)]
	* 
	* @param byteBuffer the Byte Buffer, for the serialisation of the Merkle Tree
	*/
	public void serializeBreadthFirst(ByteBuffer byteBuffer) {
		
		Queue<Node> nodesQueue = new ArrayDeque<Node>( ( ( numNodes / 2 ) + 1 ) );
    
		nodesQueue.add(this.treeRoot);
	    
	  
		while ( !nodesQueue.isEmpty() ) {

			Node node = nodesQueue.remove();
	      
			byteBuffer.put(node.type).putInt(node.signatureHash.length).put(node.signatureHash);
	      
	      
			if (node.left != null) {
	    		
				nodesQueue.add(node.left);
	    		
			}
	    	
		  
			if (node.right != null) {
	    	
				nodesQueue.add(node.right);
	    	
			}
	    
		}
  
	}

   /**
    * Create a Merkle Tree from the bottom up starting from the Leafs' Signatures.
	* 
	* @param signatures the Leafs' Signatures
 * @throws NoSuchPaddingException 
 * @throws BadPaddingException 
 * @throws IllegalBlockSizeException 
 * @throws NoSuchAlgorithmException 
 * @throws InvalidKeyException 
	*/
	public void constructTree(List<String> leafsSignatures) throws InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
	
		if (leafsSignatures.size() <= 1) {
			
			  throw new IllegalArgumentException("Must be at least two signatures to construct a Merkle Tree!!!");
		  
  	    }
	    
		this.leafsSignatures = leafsSignatures;
		this.numNodes = leafsSignatures.size();
		
		
		List<Node> parentsNodes = this.bottomLevel(leafsSignatures);
		  
		this.numNodes += parentsNodes.size();
		  
		this.treeDepth = 1;
		    
		
		while (parentsNodes.size() > 1) {
		  
			parentsNodes = this.internalLevel(parentsNodes);
		     
			this.treeDepth++;
		      
			this.numNodes += parentsNodes.size();
		    
	    }
		  
		this.treeRoot = parentsNodes.get(0);
	  
	}

  
	public int getNumNodes() {
    
		return this.numNodes;
  
	}
  
	public Node getTreeRoot() {
   
		return this.treeRoot;
  
	}
  
	public int getHeight() {
		  
		return this.treeDepth;
  
	}
  

	/**
	 * Constructs an Internal Level of the Merkle Tree.
	 * @throws NoSuchPaddingException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 */
	private List<Node> internalLevel(List<Node> childrenNodes) throws InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
	    
		List<Node> parentsNodes = new ArrayList<Node>(childrenNodes.size() / 2);
	    
	    
		for (int i = 0; i < childrenNodes.size() - 1; i += 2) {
	      
			Node childrenNode1 = childrenNodes.get(i);
			Node childrenNode2 = childrenNodes.get( ( i + 1 ) );
	      
			Node parent = constructInternalNode(childrenNode1, childrenNode2);
			  
			parentsNodes.add(parent);
	    
		}
	    
	    
		if (childrenNodes.size() % 2 != 0) {
			  
			Node childrenNode = childrenNodes.get( ( childrenNodes.size() - 1 ) );
			Node parentNode = constructInternalNode( childrenNode, null );
	      
			parentsNodes.add(parentNode);
			  
	    }
	    
		return parentsNodes;
	  
	}

  
	/**
	 * Constructs the bottom part of the tree - the leaf nodes and their
	 * immediate parents.  Returns a list of the parent nodes.
	 * @throws NoSuchPaddingException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 */
	private List<Node> bottomLevel(List<String> leafsSignatures) throws InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
	    
		List<Node> parentsNodes = new ArrayList<Node>(leafsSignatures.size() / 2);
	    
	    
		for (int i = 0; i < ( leafsSignatures.size() - 1 ); i += 2) {
			
			Node leafNode1 = constructLeafNode( leafsSignatures.get( i ) );
			Node leafNode2 = constructLeafNode( leafsSignatures.get( ( i + 1 ) ) );
	      
			Node parentNode = this.constructInternalNode(leafNode1, leafNode2);
			  
			parentsNodes.add(parentNode);
			
	    }
	    
		
		// If odd number of leafs, handle last entry
		if ( ( leafsSignatures.size() % 2 ) != 0) {
	      
			Node leafNode = constructLeafNode( leafsSignatures.get( ( leafsSignatures.size() - 1 ) ) );      
	      
			Node parentnode = this.constructInternalNode(leafNode, null);
	      
			parentsNodes.add(parentnode);
			
	    }
	    
	    
		return parentsNodes;
	  
    }

	private Node constructInternalNode(Node childrenNode1, Node childrenNode2) throws InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
		  
		Node parentNode = new Node();
		  
		parentNode.type = INTERNAL_SIG_TYPE;
    
		   
		if (childrenNode2 == null) {
		  
			parentNode.signatureHash = childrenNode1.signatureHash;
		    
	    }
		else {
		  
			parentNode.signatureHash = internalHash(childrenNode1.signatureHash, childrenNode2.signatureHash);
		    
	    }
    
    
		parentNode.left = childrenNode1;
    
		parentNode.right = childrenNode2;
    
		return parentNode;
  
	}

	private static Node constructLeafNode(String signature) {
	    
		Node leafNode = new Node();
	    
		leafNode.type = LEAF_SIG_TYPE;
	    
		leafNode.signatureHash = signature.getBytes(StandardCharsets.UTF_8);
	    
		return leafNode;
	  
	}
  
	private byte[] internalHash(byte[] leftChildrenSignatureHash, byte[] rightChildrenSignatureHash) throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
	    
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		
		byte[] parentSerialized = new byte[ ( leftChildrenSignatureHash.length + rightChildrenSignatureHash.length ) ];
		
		System.arraycopy(leftChildrenSignatureHash, 0, parentSerialized, 0, leftChildrenSignatureHash.length);
		System.arraycopy(rightChildrenSignatureHash, 0, parentSerialized, leftChildrenSignatureHash.length, rightChildrenSignatureHash.length);
		
		byte[] parentHash = messageDigest.digest(parentSerialized);
		
		Cipher cipher = Cipher.getInstance("RSA");
		
		Key privateKey = null; //TODO
		
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		
		
		return cipher.doFinal(parentHash);
		
		//this.crc.reset();
		  
	    
		//this.crc.update(leftChildrenSignatureHash);
	    
		//this.crc.update(rightChildrenSignatureHash);
		  
	    
		//return longToByteArray(crc.getValue());
	  
	}

  
	// Node Class
	  
	/**
	 * The Node class should be treated as immutable, though immutable
	 * is not enforced in the current design.
	 * 
	 * A Node knows whether it is an internal or leaf node and its signature.
	 * 
	 * Internal Nodes will have at least one child (always on the left).
	 * Leaf Nodes will have no children (left = right = null).
	 */
	public static class Node {
		  
		public byte type;  // INTERNAL_SIG_TYPE or LEAF_SIG_TYPE
		public byte[] signatureHash; // Signature Hash of the Node
		  
		public Node left;
		public Node right;
	    
		@Override
	    public String toString() {
			  
			String leftType = "<null>";
			String rightType = "<null>";
	      
			if (left != null) {
				
				leftType = String.valueOf(left.type);
			  
			}
	      
			if (right != null) {
	        
				rightType = String.valueOf(right.type);
			
			}
	      
			return String.format("MerkleTree.Node<type:%d, sig:%s, left (type): %s, right (type): %s>",
								 type, sigAsString(), leftType, rightType);
	    
		}
	
		private String sigAsString() {
			  
			StringBuffer stringBuffer = new StringBuffer();
	      
			stringBuffer.append('[');
	      
			  
			for (int i = 0; i < signatureHash.length; i++) {
				
				stringBuffer.append(signatureHash[i]).append(' ');
			  
			}
	      
			  
			stringBuffer.insert( ( stringBuffer.length() - 1 ), ']');
	      
			  
			return stringBuffer.toString();
	    
		}
	  
	}  
  
	  
	/**
	 * Performs and Big-Endian conversion from Long to Byte Array.
	 */
	public static byte[] longToByteArray(long value) {
		  
		return new byte[] {
	 		
				(byte) (value >> 56),
	   			(byte) (value >> 48),
				(byte) (value >> 40),
				(byte) (value >> 32),
				(byte) (value >> 24),
	            (byte) (value >> 16),
	            (byte) (value >> 8),
	            (byte) value
	              
		  };
	  
	}
	
}