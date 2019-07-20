package lk.css.garmentManagement.resources.companyAgent.service;

import lk.CSS.GarmentManagement.resources.companyAgent.entity.Agent;
import lk.CSS.GarmentManagement.util.interfaces.AbstractService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AgentService implements AbstractService<Agent, Long> {
    @Override
    public List<Agent> findAll() {
        return null;
    }

    @Override
    public Agent findById(Long id) {
        return null;
    }

    @Override
    public Agent persist(Agent agent) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public List<Agent> search(Agent agent) {
        return null;
    }
}
