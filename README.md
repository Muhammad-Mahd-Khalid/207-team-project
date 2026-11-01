# Project Name
## Team Information
### Team Members
- Boheng Jia
- Reyna Qin
- Muhammad Mahd Khalid
- Jiayou Zhu
---
## Project Description
### Domain : Market Data Analysis 
This project aims to provide a cohesive and centralized access to eBay Market Data for use by both Sellers and Buyers, providing a more complete and intuivitive approach to market research on an item-to-item basis.   
---
## User Stories
### User Story 1
### User Story 2: As a seller who wants to sell multiple items, I want to receive a ranked list of selling priorities so that I can decide which items to sell first and use my time more efficiently.
### User Story 3: As a buyer, I'd like to view pricing and quality data for an item I'm considering purchasing so I can set realistic spending expectations.
### User Story 4
(Add additional user stories as needed.)
---
## Use Cases
### Use Case 1
### Use Case 2: Generate Selling Priority Recommendations
Primary Actor: Seller who wants to sell multiple items.

Preconditions:

- The user is in the Seller view of the application.

- The seller has multiple items they are considering selling.

Trigger: The seller chooses to analyze multiple items and generate a selling priority list.

Success Outcome: The system analyzes the available market information and the estimated preparation effort for each item, then presents a ranked list to help the seller decide which items to consider selling first.

Main Success Scenario:

1. The seller enters the names and relevant information of the items they want to sell.

2. The system retrieves comparable active listings for each item using the eBay Browse API.

3. The system analyzes available market indicators to estimate each item's market potential.

4. The seller enters the estimated preparation time required for each item.

5. The system combines the market potential and preparation efficiency scores to calculate an overall priority score for each item. The system ranks the items based on their priority scores.

6. The system displays the ranked list, helping the seller decide which item to list first.

Extensions:

- 2a. No comparable listings are found for an item. 
  - i. The system notifies the seller that sufficient market data is unavailable for that item.

  - ii. The item is excluded from market-based scoring or marked as having insufficient data.

- 5a. Insufficient market data is available for an item.

  - i. The system informs the seller that its market potential cannot be reliably evaluated.

  - ii. The system avoids presenting an unsupported market score for that item.
### Use Case 3: Get item pricing data
Primary Actor: Buyer performing an item search

Preconditions: 
- The user is in the Buyer view of the application.

Trigger: The buyer enters the name of the item they'd like pricing data on into the system.

Success Outcome: The appropriate data is recieved, processed, analyzed and presented to the buyer succintly.

Main Success Scenario:
1. The user enters a product name.
2. The system checks whether data exists for that item.
3. The system retrieves this data and processes the information to generate analytics.
4. The system displays the final pricing data for the user.

Extensions:

- 2a. There is no valid data for this item.
  - i. The system displays an error to notify the user that no data was found for this item.
  - ii. No data processing occurs and the Use Case exits early.

### Use Case 4
(Add additional use cases as needed.)
---
## Minimal Viable Product (MVP)
| Team Member | MVP Use Case |
|-------------|--------------|
| Name | Use Case |
| Name | Use Case |
| Name | Use Case |
| Name | Use Case |
---
## Entities
### Entity 1
### Entity 2
(Add additional entities as needed.)
---
## API Usage
**API Name:**
**Documentation:** [Link]
**How it is used in the program:**
---
## Design Diagrams
### UML Class Diagram
### Sequence Diagrams
---
## How to Run
[Instructions for running the program.]
---
## Notes
[Any additional project information.]