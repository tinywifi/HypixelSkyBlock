package net.swofty.service.bazaar;

import net.swofty.commons.ServiceType;
import net.swofty.service.generic.SkyBlockService;
import net.swofty.service.generic.redis.ServiceEndpoint;

import java.util.List;

public class BazaarService implements SkyBlockService {

    public static void main(String[] args) {
        SkyBlockService.init(new BazaarService());
    }

    @Override
    public ServiceType getType() {
        return ServiceType.BAZAAR;
    }

    @Override
    public List<ServiceEndpoint> getEndpoints() {
        return loopThroughPackage("net.swofty.service.bazaar.endpoints", ServiceEndpoint.class).toList();
    }
}
