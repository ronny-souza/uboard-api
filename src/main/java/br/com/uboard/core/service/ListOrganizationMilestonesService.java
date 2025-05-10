package br.com.uboard.core.service;

import br.com.uboard.core.model.Milestone;
import br.com.uboard.core.model.filters.MilestoneFiltersDTO;
import br.com.uboard.core.model.transport.MilestoneDTO;
import br.com.uboard.core.model.transport.SessionUserDTO;
import br.com.uboard.core.repository.MilestoneRepository;
import br.com.uboard.core.repository.specification.MilestoneSpecification;
import br.com.uboard.core.repository.specification.builder.MilestoneSpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ListOrganizationMilestonesService {
    private final MilestoneRepository milestoneRepository;

    public ListOrganizationMilestonesService(MilestoneRepository milestoneRepository) {
        this.milestoneRepository = milestoneRepository;
    }

    // FIX: QUANDO IMPLEMENTAR OS MEMBROS DA ORGANIZAÇÃO, DEVE CHECAR SE O USUÁRIO DA SESSÃO É DONO OU MEMBRO, E SE TEM
    // PERMISSÃO PARA LISTAR
    public Page<MilestoneDTO> listMilestonesAsPage(Pageable pageable,
                                                   SessionUserDTO sessionUserDTO,
                                                   String organizationId,
                                                   MilestoneFiltersDTO filters) {

        Specification<Milestone> specifications = MilestoneSpecificationBuilder.builder()
                .withSpecification(MilestoneSpecification.belongsToOrganization(organizationId))
                .withFilters(filters)
                .build();
        return this.milestoneRepository.findAll(specifications, pageable).map(MilestoneDTO::new);
    }
}
