package com.example.demo.exception;

import com.example.demo.exception.comment.CommentNotFoundException;
import com.example.demo.exception.news.NewsNotFoundException;
import com.example.demo.exception.request.BadRequestParametersException;
import com.example.demo.exception.request.NotEnoughRightsException;
import com.example.demo.exception.user.UserNotFoundException;
import com.example.demo.exception.user.UsernameReservedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;

/**
 * This class handling all Throwable on controller level,
 * includes custom runtime exceptions and returns {@link ErrorInfo}
 */
@RestControllerAdvice
public class WebApplicationExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Handle {@link CommentNotFoundException}, thrown by {@link com.example.demo.controller.CommentController}
     */
    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorInfo commentNotFoundHandler(HttpServletRequest req, Exception ex) {
        logger.debug(ex.getMessage());
        return new ErrorInfo(req.getRequestURL().toString(), ex.getMessage());
    }

    /**
     * Handle {@link NewsNotFoundException}, thrown by {@link com.example.demo.controller.NewsController}
     */
    @ExceptionHandler(NewsNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorInfo newsNotFoundHandler(HttpServletRequest req, Exception ex) {
        logger.debug(ex.getMessage());
        return new ErrorInfo(req.getRequestURL().toString(), ex.getMessage());
    }

    /**
     * Handle {@link UserNotFoundException}, thrown by {@link com.example.demo.controller.UserController}
     */
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorInfo userNotFoundHandler(HttpServletRequest req, Exception ex) {
        logger.debug(ex.getMessage());
        return new ErrorInfo(req.getRequestURL().toString(), ex.getMessage());
    }

    /**
     * Handle {@link UsernameReservedException}, thrown by {@link com.example.demo.controller.UserController}
     */
    @ExceptionHandler(UsernameReservedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorInfo usernameReservedHandler(HttpServletRequest req, Exception ex) {
        logger.debug(ex.getMessage());
        return new ErrorInfo(req.getRequestURL().toString(), ex.getMessage());
    }

    /**
     * Handle {@link BadRequestParametersException}, what means request can't be performed with chosen parameters
     */
    @ExceptionHandler(BadRequestParametersException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorInfo badRequestParametersHandler(HttpServletRequest req, Exception ex) {
        logger.debug(ex.getMessage());
        return new ErrorInfo(req.getRequestURL().toString(), ex.getMessage());
    }

    /**
     * Handle {@link NotEnoughRightsException}, what means request can't be performed by current user
     */
    @ExceptionHandler(NotEnoughRightsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorInfo notEnoughRightsHandler(HttpServletRequest req, Exception ex) {
        logger.debug(ex.getMessage());
        return new ErrorInfo(req.getRequestURL().toString(), ex.getMessage());
    }

    /**
     * Handle {@link MethodArgumentTypeMismatchException}, what means request or some it's parameters can't be parsed
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorInfo badRequestSyntax(HttpServletRequest req, Exception ex) {
        logger.debug(ex.getMessage());
        return new ErrorInfo(req.getRequestURL().toString(), ex.getMessage());
    }
}
