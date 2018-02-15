package com.pjeng.memberadminservice.exception;

import java.sql.SQLException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

@ControllerAdvice
@RequestMapping(produces = "application/vnd.error+json")
public class GlobalDefaultExceptionHandler {

	private final static Logger logger = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class.getName());

	@ExceptionHandler({ DataIntegrityViolationException.class, MethodArgumentNotValidException.class })
	public ResponseEntity<VndErrors> dataConflict(final Exception e) {
		logger.error(e.getLocalizedMessage());
		return error(e, HttpStatus.CONFLICT, "Data Integrity Violation");
	}

	@ExceptionHandler({ SQLException.class, DataAccessException.class })
	public ResponseEntity<VndErrors> databaseError(final Exception e) {
		logger.error(e.getLocalizedMessage());
		return error(e, HttpStatus.INTERNAL_SERVER_ERROR, "Database Error");
	}

	@ExceptionHandler(MemberNotFoundException.class)
	public ResponseEntity<VndErrors> notFoundException(final MemberNotFoundException e) {
		return error(e, HttpStatus.NOT_FOUND, e.getLocalizedMessage());
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<VndErrors> notSupported(final Exception e) {
		return error(e, HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported Media Type");
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<VndErrors> notReadable(final Exception e) {
		return error(e, HttpStatus.BAD_REQUEST, "Problems Parsing JSON");
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<VndErrors> assertionException(final IllegalArgumentException e) {
		return error(e, HttpStatus.NOT_FOUND, e.getLocalizedMessage());
	}

	private ResponseEntity<VndErrors> error(final Exception exception, final HttpStatus httpStatus,
			final String message) {
		final String logRef = message + "-" + Optional.of(exception.getClass().getSimpleName());
		return new ResponseEntity<>(new VndErrors(logRef, message), httpStatus);
	}
}
