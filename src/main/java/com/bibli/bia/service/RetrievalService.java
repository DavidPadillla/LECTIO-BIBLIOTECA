package com.bibli.bia.service;

import com.bibli.bia.Model.DocumentChunk;
import com.bibli.bia.util.TextSimilarityUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RetrievalService {

    private final List<DocumentChunk> chunks = new ArrayList<>();

    @PostConstruct
    public void cargarDocumentos() {
        try {

            String[] archivos = {
                    "documents/biblioteca.txt",
                    "documents/reglamento.txt",
                    "documents/ia_rag.txt"
            };

            for (String archivo : archivos) {

                ClassPathResource resource = new ClassPathResource(archivo);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)
                );

                String contenido = reader.lines().collect(Collectors.joining("\n"));

                // Dividir por párrafos
                String[] partes = contenido.split("\n\n");

                for (String parte : partes) {

                    if (!parte.isBlank()) {

                        chunks.add(
                                new DocumentChunk(
                                        parte.trim(),
                                        archivo,
                                        0.0
                                )
                        );
                    }
                }
            }

            System.out.println(" Documentos cargados: " + chunks.size());

        } catch (Exception e) {
            System.err.println(" Error cargando documentos: " + e.getMessage());
        }
    }

    public List<DocumentChunk> buscarChunksRelevantes(String pregunta, int topK) {

        List<DocumentChunk> resultados = new ArrayList<>();

        for (DocumentChunk chunk : chunks) {

            double similitud = TextSimilarityUtil.cosineSimilarity(
                    pregunta,
                    chunk.getContent()
            );

            resultados.add(
                    new DocumentChunk(
                            chunk.getContent(),
                            chunk.getSource(),
                            similitud
                    )
            );
        }

        return resultados.stream()
                .sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
                .limit(topK)
                .collect(Collectors.toList());
    }
}