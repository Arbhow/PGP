package org.kxsj.com.property;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@ConfigurationProperties(prefix = "kxsj.ftp")
@Data
@EqualsAndHashCode(callSuper = false)
@Order(-1)
public class FtpPropert {
    private String hostname;
    private Integer port;
    private String username;
    private String password;
}
