package xoshnik.controllers;

import com.siebel.CustomUI.ATC_spcMTT_spcUpdate_spcCall_spcStatus_spcHelper_PortType;
import com.siebel.CustomUI.ATC_spcMTT_spcUpdate_spcCall_spcStatus_spcHelper_ServiceLocator;
import java.rmi.RemoteException;
import java.util.Arrays;
import javax.servlet.ServletContext;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.holders.StringHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xoshnik.dto.MttDTO;

@Slf4j
@RestController
@RequiredArgsConstructor
public class InvokerController {

    private final ServletContext servletContext;

    private ATC_spcMTT_spcUpdate_spcCall_spcStatus_spcHelper_ServiceLocator locator;

    @PostMapping(value = "jsontosoap")
    public ResponseEntity<Object> invokeSoapWithJson(@RequestBody MttDTO dto) {
        try {
            ATC_spcMTT_spcUpdate_spcCall_spcStatus_spcHelper_PortType portType = getPort();
            portType.ATC_spcMTT_spcUpdate_spcCall_spcStatus_spcHelper(
                new StringHolder(dto.getOrderNumber()),
                new StringHolder(dto.getStatus()),
                new StringHolder(dto.getDate()),
                new StringHolder(dto.getAnswer()),
                new StringHolder(dto.getPhone()),
                new StringHolder(dto.getRequestId()),
                new StringHolder(dto.getCallid())
            );
        } catch (RemoteException | ServiceException ex) {
            log.error(ex.getLocalizedMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getLocalizedMessage() + "\n"
                + Arrays
                .toString(ex.getStackTrace()));
        }
        return ResponseEntity.ok().build();
    }

    private ATC_spcMTT_spcUpdate_spcCall_spcStatus_spcHelper_PortType getPort() throws ServiceException {
        if (locator == null) {
            locator = new ATC_spcMTT_spcUpdate_spcCall_spcStatus_spcHelper_ServiceLocator();
            locator.setATC_spcMTT_spcUpdate_spcCall_spcStatus_spcHelperEndpointAddress(servletContext
                .getInitParameter("siebel_server_url"));
        }
        return locator
            .getATC_spcMTT_spcUpdate_spcCall_spcStatus_spcHelper();
    }

}
