package fr.insy2s.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class MaterialTypeMapperTest {

    private MaterialTypeMapper materialTypeMapper;

    @BeforeEach
    public void setUp() {
        materialTypeMapper = new MaterialTypeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(materialTypeMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(materialTypeMapper.fromId(null)).isNull();
    }
}
