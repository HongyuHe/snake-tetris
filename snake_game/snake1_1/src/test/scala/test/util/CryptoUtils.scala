// DO NOT MODIFY FOR BASIC SUBMISSION
// scalastyle:off

package test.util

import java.nio.charset.StandardCharsets.UTF_8
import java.security.spec.{PKCS8EncodedKeySpec, X509EncodedKeySpec}
import java.security.{KeyFactory, PrivateKey, PublicKey, Signature}
import java.util.Base64

import javax.crypto.Cipher

import scala.io.{Codec, Source}

/** Lazily adapted from Java:
  * https://adangel.org/2016/08/29/openssl-rsa-java/
  */
private[test] object CryptoUtils {

  val keyName = "grader"

  @throws[Exception]
  lazy val getPublicKey: PublicKey = {
    var publicKeyPEM: String =
      using(Source.fromFile(s"$keyName-pub.pem")(Codec.UTF8)) {
        src => src.getLines.mkString
      }

    publicKeyPEM = publicKeyPEM
      .replace("-----BEGIN PUBLIC KEY-----", "")
      .replace("-----END PUBLIC KEY-----", "")
      .replaceAll("\\s", "")

    val publicKeyDER = Base64.getDecoder.decode(publicKeyPEM)
    val keyFactory = KeyFactory.getInstance("RSA")
    val publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyDER))

    publicKey
  }

  @throws[Exception]
  lazy val getPrivateKey: PrivateKey = {
    var privateKeyPEM: String =
      using(Source.fromFile(s"$keyName-pkcs8.pem")(Codec.UTF8)) {
        src => src.getLines.mkString
      }

    privateKeyPEM = privateKeyPEM
      .replace("-----BEGIN PRIVATE KEY-----", "")
      .replace("-----END PRIVATE KEY-----", "")
      .replaceAll("\\s", "")

    val privateKeyDER = Base64.getDecoder.decode(privateKeyPEM)
    val keyFactory = KeyFactory.getInstance("RSA")
    val privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyDER))

    privateKey
  }

  @throws[Exception]
  def encrypt(plainText: String): String = {
    val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
    val publicKey = getPublicKey
    cipher.init(Cipher.ENCRYPT_MODE, publicKey)

    val crypt = cipher.doFinal(plainText.getBytes(UTF_8))
    Base64.getEncoder.encodeToString(crypt)
  }

  @throws[Exception]
  def decrypt(cipherText: String): String = {
    val crypt = Base64.getDecoder.decode(cipherText)
    val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
    val privateKey = getPrivateKey
    cipher.init(Cipher.DECRYPT_MODE, privateKey)

    new String(cipher.doFinal(crypt), UTF_8)
  }

  @throws[Exception]
  def sign(plainText: String): String = {
    val privateSignature = Signature.getInstance("SHA256withRSA")
    privateSignature.initSign(getPrivateKey)
    privateSignature.update(plainText.getBytes(UTF_8))

    val signature = privateSignature.sign
    Base64.getEncoder.encodeToString(signature)
  }

  @throws[Exception]
  def verify(plainText: String, signature: String): Boolean = {
    val publicSignature = Signature.getInstance("SHA256withRSA")
    publicSignature.initVerify(getPublicKey)
    publicSignature.update(plainText.getBytes(UTF_8))

    val signatureBytes = Base64.getDecoder.decode(signature)
    publicSignature.verify(signatureBytes)
  }

  def using[A <: {def close(): Unit}, B](resource: A)(f: A => B): B =
    try {
      f(resource)
    } finally {
      resource.close()
    }

}
