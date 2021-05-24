package sk.majo.attendancesystem.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sk.majo.attendancesystem.services.CryptoCode;

@RestController
@RequestMapping(path = "/api/help")
public class HelperController {

    final private static Logger logger = LoggerFactory.getLogger(HelperController.class);

    @RequestMapping(path = "/encodeString", method = RequestMethod.POST)
    public String encodeString(@RequestBody String value) {

        try {
            CryptoCode cryptoCode = new CryptoCode();
            logger.info("String To Encrypt: "+ value);
            String encrypted = cryptoCode.encrypt(value);
            logger.info("Encrypted String:" + encrypted);
            String decrypted = cryptoCode.decrypt(encrypted);
            logger.info("Decrypted String:" + decrypted);

            return encrypted;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    @RequestMapping(path = "/decodeString", method = RequestMethod.POST)
    public String decodeString(@RequestBody String value) {

        try {
            CryptoCode cryptoCode = new CryptoCode();
            logger.info("String To Decode: "+ value);
            String decrypted = cryptoCode.decrypt(value);
            logger.info("Decrypted String:" + decrypted);

            return decrypted;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }


}
