package rzaevali.algo;

import technology.tabula.*;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LeftToRightExtractionAlgorithm extends SpreadsheetExtractionAlgorithm {
    @Override
    public List<Table> extract(Page page, List<Ruling> rulings) {
        // split rulings into horizontal and vertical
        List<Ruling> horizontalR = new ArrayList<>(),
                verticalR = new ArrayList<>();

        for (Ruling r : rulings) {
            if (r.horizontal()) {
                horizontalR.add(r);
            } else if (r.vertical()) {
                verticalR.add(r);
            }
        }
        horizontalR = Ruling.collapseOrientedRulings(horizontalR);
        verticalR = Ruling.collapseOrientedRulings(verticalR);

        List<Cell> cells = findCells(horizontalR, verticalR);
        List<Rectangle> spreadsheetAreas = findSpreadsheetsFromCells(cells);

        List<Table> spreadsheets = new ArrayList<>();
        for (Rectangle area : spreadsheetAreas) {

            List<Cell> overlappingCells = new ArrayList<>();
            for (Cell c : cells) {
                if (c.intersects(area)) {

                    // skip text elements with non left-to-right direction
                    // this prevents merging vertical and horizontal text
                    c.setTextElements(
                            TextElement.mergeWords(page.getText(c)
                                    .stream()
                                    .filter(textElement -> textElement.getDirection() == 0.0)
                                    .collect(Collectors.toList()))
                    );
                    overlappingCells.add(c);
                }
            }

            List<Ruling> horizontalOverlappingRulings = new ArrayList<>();
            for (Ruling hr : horizontalR) {
                if (area.intersectsLine(hr)) {
                    horizontalOverlappingRulings.add(hr);
                }
            }
            List<Ruling> verticalOverlappingRulings = new ArrayList<>();
            for (Ruling vr : verticalR) {
                if (area.intersectsLine(vr)) {
                    verticalOverlappingRulings.add(vr);
                }
            }

            TableWithRulingLines t = new TableWithRulingLines(area, overlappingCells, horizontalOverlappingRulings, verticalOverlappingRulings, this);
            spreadsheets.add(t);
        }
        Utils.sort(spreadsheets, Rectangle.ILL_DEFINED_ORDER);
        return spreadsheets;
    }
}
