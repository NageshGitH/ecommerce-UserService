package com.userregistration.application.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_EMPTY)
public class OrderProductDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(position = 1)
	@NotEmpty(message = "provide user name")
	@Size(min = 5, max = 50)
	@Pattern(regexp = "[a-zA-Z0-9]+")
	private String userName;

	@ApiModelProperty(position = 2)
	@NotNull(message = "provide account no ,only digits")
	@Pattern(regexp = "[0-9]{12}", message = "provide a valid account no")
	private String accountNo;

	@ApiModelProperty(position = 3)
	@NotNull(message = "provide cart ,only digits")
	@Pattern(regexp = "^[0-9]*$",message = "provide a valid cart")
	private String cartId;
	
	private String userId;

}
