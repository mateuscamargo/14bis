package com.fingerone.BisServer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fingerone.BisServer.entity.PdfPage;
import com.fingerone.BisServer.message.ResponseMessage;
import com.fingerone.BisServer.model.ProcessPdfResponse;
import com.fingerone.BisServer.repository.PdfPageRepository;
import com.fingerone.BisServer.service.PdfPageService;

import io.swagger.annotations.Api;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("pdfpage")
@Api(value = "PDF Page")
public class PdfPageController {
	@Autowired
	private PdfPageRepository pdfPageRepository;

	@Autowired
	private PdfPageService pdfExtractorService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PdfPage>> listAll() {
		List<PdfPage> pdfPageList = (List<PdfPage>) pdfPageRepository.findAll();
		if (!pdfPageList.isEmpty())
			return new ResponseEntity<>(pdfPageList, HttpStatus.OK);
		else
			return new ResponseEntity<>(pdfPageList, HttpStatus.NOT_FOUND);
	}

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProcessPdfResponse> process(@RequestParam String manualName) {
		try {
			ProcessPdfResponse response = pdfExtractorService.extractPagesOfPdfs(manualName);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity<>(new ProcessPdfResponse(), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = "/novoPDF", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseMessage> createNewPDF(@RequestParam String manualName, @RequestParam String remark,
			@RequestParam String docType) {
		try {
			pdfExtractorService.createNewPdf(manualName, remark, docType);

			return new ResponseEntity<ResponseMessage>(new ResponseMessage("PDF criado com sucesso"), HttpStatus.OK);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity<>(new ResponseMessage("Falha ao criar novo PDF"), HttpStatus.BAD_REQUEST);
		}
	}
}