package fr.insy2s.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class MaterialRequestMapperTest {

    private MaterialRequestMapper materialRequestMapper;

    @BeforeEach
    public void setUp() {
        materialRequestMapper = new MaterialRequestMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(materialRequestMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(materialRequestMapper.fromId(null)).isNull();
    }
}
