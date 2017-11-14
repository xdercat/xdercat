package com.xdercat.domain.defined;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by bibom on 11/14/17.
 */
@JsonNaming(PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy.class)
@Data
public class TestDO implements Serializable {
    private static final long serialVersionUID = -4750391482192275567L;

    private Integer id;

}
